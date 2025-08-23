package com.study.hdrdemo.utils;

import android.os.Environment;

import java.io.File;

public class TestFile {

    public static final int FILE_PATH_TYPE_SDCARD = 0;
    public static final int FILE_PATH_TYPE_ASSET = 1;
    public static int filePathType;
    private static String filePath;
    public static  File file;

    public static String getFilePath() {
        String subdirectory;
        switch (filePathType) {
            case FILE_PATH_TYPE_ASSET:
                subdirectory = "/android_asset";
                break;
            default:
                subdirectory = Environment.getExternalStorageDirectory().getPath();
                break;
        }
//        filePath = "file://" + subdirectory + "/apple_apple_gainmap.jpg";
//        filePath = "file://" + subdirectory + "/apple_iso_gainmap.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_01.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_02.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_03.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_04.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_05.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_06.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_07.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_08.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_09.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_10.jpg";
//        filePath = "file://" + subdirectory + "/jpg_hdr_mi.jpg";
        filePath = "file://" + subdirectory + "/hdr_01.heic";
//        filePath = "file://" + subdirectory + "/hdr_02.heic";
//        filePath = "file://" + subdirectory + "/hdr_03.heic";
//        filePath = "file://" + subdirectory + "/hdr_04.heic";
//        filePath = "file://" + subdirectory + "/hdr_05.heic";
//        filePath = "file://" + subdirectory + "/hdr_06.heic";
//        filePath = "file://" + subdirectory + "/hdr_07.heic";
//        filePath = "file://" + subdirectory + "/hdr_08.heic";
//        filePath = "file://" + subdirectory + "/hdr_09.heic";
//        filePath = "file://" + subdirectory + "/hdr_10.heic";
        return filePath;
    }

    public static File getFile() {
        file = new File(Environment.getExternalStorageDirectory().getPath(), "jpg_hdr_01.jpg");
        file = new File(Environment.getExternalStorageDirectory().getPath(), "jpg_hdr_01.jpg");
        file = new File(Environment.getExternalStorageDirectory().getPath(), "jpg_hdr_mi.jpg");
        return file;
    }
}
