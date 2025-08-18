package com.study.hdrdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.TextureView;

import androidx.annotation.RequiresApi;

import com.study.hdrdemo.utils.GainmapReflector;
import com.study.hdrdemo.utils.TestFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureTextureView extends TextureView implements android.view.TextureView.SurfaceTextureListener {

    private static final String TAG = "debug_hdr";
    private Paint mPaint;//画笔
    private Rect mSrcRect;
    private Rect mDstRect;
    private Thread thread;//用于绘制的线程
    private boolean isRunning;//线程的控制开关

    private Bitmap mBitmap;
    int mWidth;
    int mHeight;

    public PictureTextureView(Context context) {
        this(context, null);
    }

    public PictureTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public PictureTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.i(TAG, ": init() ");

        setSurfaceTextureListener(this);//设置监听
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//创建画笔

        mSrcRect = new Rect();
        mDstRect = new Rect();
    }
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i(TAG,"PictureTextureView : onSurfaceTextureAvailable()");
        Log.i(TAG,"PictureTextureView : width = " + width);
        Log.i(TAG,"PictureTextureView : height = " + height);
        mWidth = width;
        mHeight = height;
        isRunning = true;

        //开启线程
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    String path = TestFile.getFilePath();
                    try {
                        mBitmap = readBitmap(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ColorSpace colorSpace = mBitmap.getColorSpace();
                    Bitmap.Config config = mBitmap.getConfig();

                    Log.d(TAG, "run: config = " + config + ", colorSpace = " + colorSpace);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        mergeBitmap_TB(mBitmap, mBitmap);
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.i(TAG,": onSurfaceTextureSizeChanged()");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.i(TAG,": onSurfaceTextureDestroyed()");
        isRunning = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Log.i(TAG,": onSurfaceTextureUpdated()");
    }

    private Bitmap readBitmap(String filePath) throws IOException {
        Log.i(TAG, ": readBitmap() ");
//        return BitmapFactory.decodeStream(getResources().getAssets().open(path));
        File file = new File(Uri.parse(filePath).getPath());
        Log.d(TAG, "loadHdrBySystemApi: filePath = " + filePath + ", file = " + file.getAbsolutePath() + ", exists = " + file.exists());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 使用广色域颜色空间以获得更好的HDR效果
//            options.inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.DISPLAY_P3);
            options.inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.BT2020_PQ); // HDR10 标准
            options.inPreferredConfig = Bitmap.Config.RGBA_F16; // 16-bit 浮点，保留 HDR 数据
            final Bitmap bitmap;
            if (true) {
                try {
                    InputStream inputStream = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    // 或者从资源解码
                    // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hdr_image, options);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            }

            if (bitmap != null) {
                Log.e(TAG, "loadHdrBySystemApi: ------bitmap getConfig = " + bitmap.getConfig()
                        + ", hasGainmap = " + GainmapReflector.hasGainmap(bitmap) + ", getColorSpace = " + bitmap.getColorSpace());
            }
            return bitmap;
        }
        return null;
    }

    private void drawBitmap_One(Bitmap bitmap) {
        Log.i(TAG, ": drawBitmap() ");
        Canvas canvas = lockCanvas();//锁定画布

        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            mSrcRect.set(0, 100, bitmap.getWidth(), bitmap.getHeight());//这里我将2个rect抽离出去，防止重复创建
            mDstRect.set(0, 100, mWidth, bitmap.getHeight() * mWidth / bitmap.getWidth());
            canvas.drawBitmap(bitmap, mSrcRect, mDstRect, mPaint);//将bitmap画到画布上
            unlockCanvasAndPost(canvas);//解锁画布同时提交
        }

    }

    /**
     * 把两个位图覆盖合成为一个位图，上下拼接
     * @param topBitmap
     * @param bottomBitmap
     *  isBaseMax 是否以高度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Bitmap mergeBitmap_TB(Bitmap topBitmap, Bitmap bottomBitmap) {

        if (topBitmap == null || topBitmap.isRecycled()
                || bottomBitmap == null || bottomBitmap.isRecycled()) {
            Log.e(TAG, "topBitmap=" + topBitmap + ";bottomBitmap=" + bottomBitmap);
            return null;
        }

        Rect topRect = new Rect(0, 0, topBitmap.getWidth(), topBitmap.getHeight()); //(0,0)->(x,y)矩阵坐标
        Rect topRectT = new Rect(0, 0, mWidth, topBitmap.getHeight() * mWidth / topBitmap.getWidth());
        Rect bottomRect  = new Rect(0, 0, bottomBitmap.getWidth(), bottomBitmap.getHeight());
        Rect bottomRectT  = new Rect(0, topBitmap.getHeight(), mWidth, bottomBitmap.getHeight() * mWidth / bottomBitmap.getWidth() + topBitmap.getHeight());

        Canvas canvas = lockCanvas();//锁定画布
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(topBitmap, topRect, topRectT, mPaint); //topRect表示图片子集；topRectT表示位图将缩放/转换以适应的矩形

            bottomBitmap.setColorSpace(ColorSpace.get(ColorSpace.Named.DCI_P3));
            canvas.drawBitmap(bottomBitmap, bottomRect, bottomRectT, mPaint);
            unlockCanvasAndPost(canvas);//解锁画布同时提交
        }
        return mBitmap;
    }
}
