package com.study.hdrdemo.utils;

import android.os.Environment;

import java.io.File;

public class TestFile {

    private static String filePath;
    public static  File file;

    public static String getFilePath() {
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/apple_apple_gainmap.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/apple_iso_gainmap.jpg";
        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_01.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_02.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_03.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_04.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_05.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_06.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_07.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_08.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_09.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_10.jpg";
//        filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/jpg_hdr_mi.jpg";
        return filePath;
    }

    public static File getFile() {
        file = new File(Environment.getExternalStorageDirectory().getPath(), "jpg_hdr_01.jpg");
        file = new File(Environment.getExternalStorageDirectory().getPath(), "jpg_hdr_01.jpg");
        file = new File(Environment.getExternalStorageDirectory().getPath(), "jpg_hdr_mi.jpg");
        return file;
    }
}
