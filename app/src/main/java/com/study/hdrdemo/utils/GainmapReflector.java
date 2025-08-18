package com.study.hdrdemo.utils;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GainmapReflector {

    private static final String TAG = "GainmapReflector";

    /**
     * 检查 Bitmap 是否包含 Gainmap (反射实现)
     */
    public static boolean hasGainmap(Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        }

        // Android 14+ 直接调用原生方法
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            return bitmap.hasGainmap();
//        }

        // 低版本使用反射
        try {
            Method method = Bitmap.class.getMethod("hasGainmap");
            return (boolean) method.invoke(bitmap);
        } catch (Exception e) {
            Log.d(TAG, "hasGainmap reflection failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取 Gainmap 对象 (反射实现)
     */
    public static Object getGainmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        // Android 14+ 直接调用原生方法
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            return bitmap.getGainmap();
//        }

        // 低版本使用反射
        try {
            Method method = Bitmap.class.getMethod("getGainmap");
            return method.invoke(bitmap);
        } catch (Exception e) {
            Log.d(TAG, "getGainmap reflection failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * 设置 Gainmap 渲染参数 (反射实现)
     */
    public static void setGainmapRenderParameters(Object gainmap, float minRatio, float maxRatio) {
        if (gainmap == null) {
            return;
        }

        try {
            // 获取 setGainmapContents 方法
            Method setContentsMethod = gainmap.getClass().getMethod("setGainmapContents",
                    Class.forName("android.graphics.Gainmap$GainmapContents"));

            // 创建 GainmapContents 参数
            Class<?> contentsClass = Class.forName("android.graphics.Gainmap$GainmapContents");
            Class<?> paramsClass = Class.forName("android.graphics.Gainmap$GainmapContents$Parameters");

            // 创建 Parameters 对象
            Object parameters = paramsClass.getConstructor(float.class, float.class, float.class, float[].class)
                    .newInstance(minRatio, maxRatio, 1.0f, new float[]{1.0f, 1.0f, 1.0f});

            // 创建 GainmapContents 对象
            Object contents = contentsClass.getConstructor(paramsClass)
                    .newInstance(parameters);

            // 调用 setGainmapContents
            setContentsMethod.invoke(gainmap, contents);
        } catch (Exception e) {
            Log.e(TAG, "setGainmapRenderParameters failed", e);
        }
    }
}