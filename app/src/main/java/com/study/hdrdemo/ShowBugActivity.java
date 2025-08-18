package com.study.hdrdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.info.ImageOriginListener;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.platform.DefaultDecoder;

import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.study.hdrdemo.BaseActivity;
import com.study.hdrdemo.utils.GainmapReflector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ShowBugActivity extends BaseActivity {
    private static final String TAG = "debug_hdr";
  TextView tipsTv;

    SimpleDraweeView draweeViewImg;
    SimpleDraweeView draweeViewImg2;
    SimpleDraweeView draweeViewImg3;
    ImageView calColorImg;

    Button playBtn;
    Button pauseBtn;
    Button stopBtn;

    private boolean isHdrSupported() {
        // 检查 HDR 支持（Android 10+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Display display = getWindowManager().getDefaultDisplay();
            Display.HdrCapabilities hdrCapabilities = display.getHdrCapabilities();
            int[] supportedHdrTypes = hdrCapabilities.getSupportedHdrTypes();

            for (int type : supportedHdrTypes) {
                String hdrType = "";
                switch (type) {
                    case Display.HdrCapabilities.HDR_TYPE_HDR10:
                        hdrType = "HDR10"; break;
                    case Display.HdrCapabilities.HDR_TYPE_HLG:
                        hdrType = "HLG"; break;
                    case Display.HdrCapabilities.HDR_TYPE_DOLBY_VISION:
                        hdrType = "Dolby Vision"; break;
                }
                Log.d(TAG, "支持的 HDR 格式: " + hdrType);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Display display = getWindowManager().getDefaultDisplay();
            return display.isHdr();
        }
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_bug);
        setContentView(R.layout.activity_show_normal);

        tipsTv = findViewById(R.id.docUrlTv);
        draweeViewImg = findViewById(R.id.draweeView);
        draweeViewImg2 = findViewById(R.id.draweeView2);
        draweeViewImg3 = findViewById(R.id.draweeView3);
        calColorImg = findViewById(R.id.cal_color_view);

        playBtn = findViewById(R.id.play);
        pauseBtn = findViewById(R.id.pause);
        stopBtn = findViewById(R.id.stop);

        getIntent().getBooleanExtra("", false);

        // 在你的 Activity 中
        Log.e(TAG, "onCreate: Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT + ", isHdrSupported() = " + isHdrSupported());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            Log.d(TAG, "onCreate: ------ setColorMode COLOR_MODE_HDR ---------");
//            getWindow().setColorMode(ActivityInfo.COLOR_MODE_HDR);
//            // 获取当前窗口的 LayoutParams
//            WindowManager.LayoutParams params = getWindow().getAttributes();
//            // 方法 1（API 31+）：直接设置 HDR 模式
////            params.setPreferredDisplayModeId(WindowManager.LayoutParams.PREFERRED_DISPLAY_MODE_HDR);
//            // 设置 HDR Headroom 值为 1.0f
//            // 所需的 HDR 预留空间量。必须大于或等于 1.0（无 HDR），
//            // 且小于或等于 10,000.0。传递 0.0 将重置为默认值，即自动选择的值。
//            // 值在 0.0f 到 10000.0 之间（包括边界值）。
////            params.setDesiredHdrHeadroom(1.0f);
//            LayoutParamsReflector.setDesiredHdrHeadroom(params, 1.0f);
//            // 应用 LayoutParams 的改变
//            getWindow().setAttributes(params);
//            // 获取并打印当前 HDR Headroom 值
//            float hdrHeadroom = -1;
////            hdrHeadroom = params.getDesiredHdrHeadroom();
//            hdrHeadroom = LayoutParamsReflector.getDesiredHdrHeadroom(params);
//            Log.d(TAG, "当前 HDR Headroom 值为：" + hdrHeadroom);
//        }

        // 检查是否支持 HDR 显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Display display = getWindowManager().getDefaultDisplay();
            Log.d(TAG, "**********display.isHdr() = ：" + display.isHdr());
            if (display.isHdr()) {
//                draweeViewImg2.setColorSpace(ColorSpace.get(ColorSpace.Named.BT2020_PQ));
            }
        }

        String filePath;
        File file;
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/test1.jpg";
//        file = new File(Environment.getExternalStorageDirectory().getPath(), "test1.jpg");
        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_01.jpg";
        file = new File(Environment.getExternalStorageDirectory().getPath(), "jpg_hdr_01.jpg");
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_mi.jpg";
//        file = new File(Environment.getExternalStorageDirectory().getPath(), "jpg_hdr_mi.jpg");

//        loadNormalBySystemApi(draweeViewImg, filePath, true);
//        loadNormalBySystemApi2(draweeViewImg, filePath, true);

//        draweeViewImg.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadHdrBySystemApi(draweeViewImg, filePath, true);
//            }
//        }, 3000);

//        loadHdrBySystemApi2(draweeViewImg2, filePath, true);

        playBtn.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: I will click!!!!");
//            startActivity(new Intent(this, ShowHDRActivity.class));
            loadHdrBySystemApi(draweeViewImg, filePath, true);
        });
    }


    private void testHdrByFresco(SimpleDraweeView simpleDraweeView, String filePath, boolean isRgba) {
        File file = new File(Uri.parse(filePath).getPath());
        Log.d(TAG, "testHdrByFresco: filePath = " + filePath + ", file = " + file.getAbsolutePath() + ", exists = " + file.exists());
        try {
            ImageRequestBuilder builder = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(filePath))
                    .disableDiskCache()
                    .disableMemoryCache()
                    .setImageDecodeOptions(
                            ImageDecodeOptions.newBuilder()
                                    .setBitmapConfig(isRgba ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565)//设置 bitmapConfig
                                    .build());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(builder.build())
                    .setOldController(simpleDraweeView.getController())
                    .setCallerContext("test_scene_tag")
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        long startTime = 0L;
                        @Override
                        public void onSubmit(String id, Object callerContext) {
                            super.onSubmit(id, callerContext);
                            startTime = System.currentTimeMillis();
                        }
                        @Override
                        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
//                            if (imageInfo instanceof CloseableStaticBitmap) {
//                                Bitmap bitmap = ((CloseableStaticBitmap) imageInfo).getUnderlyingBitmap();
//                                final Bitmap newBitmap = bitmap.copy(bitmap.getConfig(), false);
//                                Log.d(TAG, "onFinalImageSet: ----bitmap config = " + bitmap.getConfig() + ", getColorSpace = " + bitmap.getColorSpace());
//                                if (Build.VERSION.SDK_INT >= 34) {
//                                    Log.d(TAG, "onFinalImageSet: ----bitmap hasGainmap = " + GainmapReflector.hasGainmap(bitmap));
//                                    if (GainmapReflector.hasGainmap(bitmap)) {
//                                        Log.d("HDR", "图像包含增益图");
//                                        Object gainmap = GainmapReflector.getGainmap(bitmap);
//                                        Log.d(TAG, "onFinalImageSet: --- gainmap = " + gainmap);
//                                        // 处理增益图...
//                                    } else {
//                                        Log.d("HDR", "图像不包含增益图");
//                                    }
//                                }
//                                draweeViewImg.postDelayed(new Runnable() {
//                                    @SuppressLint("NewApi")
//                                    @Override
//                                    public void run() {
//                                        Log.e(TAG, "onFinalImageSet: ---@@ 11 @@---newBitmap getColorSpace = " + newBitmap.getColorSpace());
//                                        if (isHdrSupported()) {
////                                                newBitmap.setColorSpace(ColorSpace.get(ColorSpace.Named.BT2020_PQ));
//                                        }
//                                        Log.e(TAG, "onFinalImageSet: ---@@ 22 @@---newBitmap getColorSpace = " + newBitmap.getColorSpace());
//                                        Toast.makeText(ShowBugActivity.this, "显示带增益的bitmap", Toast.LENGTH_SHORT).show();
//                                        draweeViewImg.setImageBitmap(newBitmap);
//                                    }
//                                }, 1000);
//                            }
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            Log.d(TAG, "onFailure: ^^^^^^^^1111^^^^^^");
                        }

                        public void onFailure(String id, ImageRequest imageRequest, Throwable throwable) {
                            Log.d(TAG, "onFailure: ^^^^^^^^2222^^^^^^");
                        }

                        @Override
                        public void onRelease(String id) {
                            Log.d(TAG, "onRelease: ^^^^^^^^1111^^^^^^");
                        }

                        public void onRelease(String id, ImageRequest imageRequest) {
                            Log.d(TAG, "onRelease: ^^^^^^^^2222^^^^^^");
                        }
                    })
                    .setImageOriginListener(new ImageOriginListener() {
                        @Override
                        public void onImageLoaded(String controllerId, int imageOrigin, boolean successful, String ultimateProducerName) {
                            Log.d(TAG, "onImageLoaded: ********** 2222 this = " + this + ", controllerId = " + controllerId + ", imageOrigin = " + imageOrigin + ", successful = " + successful);
                        }
                    })
                    .build();
            simpleDraweeView.setController(controller);
        } catch (Exception e) {
            Log.d(TAG, "testHdr: e = " + e);
        }
    }

    private void loadNormalBySystemApi(SimpleDraweeView simpleDraweeView, String filePath, boolean useInputStream) {
        File file = new File(Uri.parse(filePath).getPath());
        Log.d(TAG, "loadNormalBySystemApi: filePath = " + filePath + ", file = " + file.getAbsolutePath() + ", exists = " + file.exists());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 使用广色域颜色空间以获得更好的HDR效果
            options.inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.SRGB);
//            options.inPreferredConfig = Bitmap.Config.RGBA_1010102;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            final Bitmap bitmap;
            if (useInputStream) {
                try {
                    InputStream inputStream = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    // 或者从资源解码
                    // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hdr_image, options);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                // 从文件路径解码
//                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            }
            Log.e(TAG, "loadNormalBySystemApi: ---111---bitmap = " + bitmap);

            if (bitmap != null) {
                Log.e(TAG, "loadNormalBySystemApi: ---222---bitmap getConfig = " + bitmap.getConfig()
                        + ", hasGainmap = " + GainmapReflector.hasGainmap(bitmap) + ", getColorSpace = " + bitmap.getColorSpace());
            }
            if (GainmapReflector.hasGainmap(bitmap)) {
                final Object gainmap = GainmapReflector.getGainmap(bitmap);
                Log.e(TAG, "loadNormalBySystemApi: ---333---gainmap = " + gainmap);
                // 现在你可以使用bitmap和gainmap进行HDR渲染
//                draweeViewImg.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(ShowBugActivity.this, "显示带增益的bitmap", Toast.LENGTH_SHORT).show();
//                        simpleDraweeView.setImageBitmap(bitmap);
//                    }
//                }, 5000);
            }
            simpleDraweeView.setImageBitmap(bitmap);
        }
    }

    private void loadNormalBySystemApi2(SimpleDraweeView simpleDraweeView, String filePath, boolean isRgba) {
        File file = new File(Uri.parse(filePath).getPath());
        Log.d(TAG, "loadNormalBySystemApi2: filePath = " + filePath + ", file = " + file.getAbsolutePath() + ", exists = " + file.exists());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ImageDecoder.Source source = ImageDecoder.createSource(file);
            try {
                Bitmap bitmap = ImageDecoder.decodeBitmap(source, new ImageDecoder.OnHeaderDecodedListener() {
                    @Override
                    public void onHeaderDecoded(@NonNull ImageDecoder decoder, @NonNull ImageDecoder.ImageInfo info, @NonNull ImageDecoder.Source source) {
                        Log.e(TAG, "loadNormalBySystemApi2: ---setTargetColorSpace  -- SRGB------ ");
                        decoder.setTargetColorSpace(ColorSpace.get(ColorSpace.Named.SRGB)); // 禁用 HDR
                        decoder.setTargetSampleSize(1); // 避免自动缩放
                    }
                });
                Log.e(TAG, "loadNormalBySystemApi2: ---111---bitmap getConfig = " + bitmap.getConfig()
                        + ", hasGainmap = " + GainmapReflector.hasGainmap(bitmap) + ", getColorSpace = " + bitmap.getColorSpace());
                simpleDraweeView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e(TAG, "loadNormalBySystemApi2: ---222---e = " + e);
            }
        }
    }

    private void loadHdrBySystemApi(final SimpleDraweeView simpleDraweeView, String filePath, boolean useInputStream) {
        File file = new File(Uri.parse(filePath).getPath());
        Log.d(TAG, "loadHdrBySystemApi: filePath = " + filePath + ", file = " + file.getAbsolutePath() + ", exists = " + file.exists());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 使用广色域颜色空间以获得更好的HDR效果
            options.inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.BT2020_PQ);
//            options.inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.BT2020_PQ); // HDR10 标准
//            options.inPreferredConfig = Bitmap.Config.RGBA_1010102;
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPreferredConfig = Bitmap.Config.RGBA_F16; // 16-bit 浮点，保留 HDR 数据
            final Bitmap bitmap;
            if (useInputStream) {
                try {
                    InputStream inputStream = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    // 或者从资源解码
                    // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hdr_image, options);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                // 从文件路径解码
//                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            }
            Log.e(TAG, "loadHdrBySystemApi: ---111---bitmap = " + bitmap);

            if (bitmap != null) {
                Log.e(TAG, "loadHdrBySystemApi: ---222---bitmap getConfig = " + bitmap.getConfig()
                        + ", hasGainmap = " + GainmapReflector.hasGainmap(bitmap) + ", getColorSpace = " + bitmap.getColorSpace());
            }
            if (GainmapReflector.hasGainmap(bitmap)) {
                final Object gainmap = GainmapReflector.getGainmap(bitmap);
                Log.e(TAG, "loadHdrBySystemApi: ---333---gainmap = " + gainmap);
                // 现在你可以使用bitmap和gainmap进行HDR渲染
                draweeViewImg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowBugActivity.this, "显示带增益的bitmap", Toast.LENGTH_SHORT).show();
                        bitmap.setColorSpace(ColorSpace.get(ColorSpace.Named.BT2020_PQ));
                        simpleDraweeView.setImageBitmap(bitmap);
                    }
                }, 10);
            }
        }
    }

    private void loadHdrBySystemApi2(SimpleDraweeView simpleDraweeView, String filePath, boolean isRgba) {
        File file = new File(Uri.parse(filePath).getPath());
        Log.d(TAG, "loadHdrBySystemApi2: filePath = " + filePath + ", file = " + file.getAbsolutePath() + ", exists = " + file.exists());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ImageDecoder.Source source = ImageDecoder.createSource(file);
            try {
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                Log.e(TAG, "loadHdrBySystemApi2: ---111---bitmap getConfig = " + bitmap.getConfig()
                        + ", hasGainmap = " + GainmapReflector.hasGainmap(bitmap) + ", getColorSpace = " + bitmap.getColorSpace());
                simpleDraweeView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e(TAG, "loadHdrBySystemApi2: ---222---e = " + e);
            }
        }
    }

    private void testPrefetch(Uri uri) {
//        Fresco.getImagePipeline().prefetchToDiskCache(ImageRequest.fromUri(uri), null);
        Fresco.getImagePipeline().prefetchToBitmapCache(ImageRequest.fromUri(uri), null);
    }

    public static void getBitmapByFresco(Uri uri, Context context) {
        ImageDecodeOptions imageDecodeOptions = ImageDecodeOptions.newBuilder().setForceStaticImage(true).build();
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setImageDecodeOptions(imageDecodeOptions)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(Bitmap bitmap) {
                //todo 这里获取到了bitmap，可直接用于view展示
                Log.d(TAG, "onNewResultImpl: bitmap = " + bitmap);
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Log.d(TAG, "onFailureImpl: dataSource = " + dataSource);
            }
        }, CallerThreadExecutor.getInstance());
    }
}
