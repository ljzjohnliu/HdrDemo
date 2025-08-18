package com.study.hdrdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 *
 *
 * @author lewin
 * @time 2020/4/15
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
    }

//    open fun getImageUrlModel(): ImageUrlModel {
//        return ViewModelProviders.of(this)[ImageUrlModel::class.java]
//    }
}