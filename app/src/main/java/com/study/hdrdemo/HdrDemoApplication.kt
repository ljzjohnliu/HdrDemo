package com.study.hdrdemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.facebook.common.logging.FLog
import com.facebook.drawee.backends.pipeline.Fresco
import com.study.hdrdemo.utils.TestFile
import org.json.JSONObject
import kotlin.collections.ArrayList


/**
 *
 *
 * @author lewin
 * @time 2020/4/14
 */
class HdrDemoApplication: Application() {
    @SuppressLint("CI_StaticFieldLeak", "StaticFieldLeak")

    companion object{
        protected var sInstance: Context? = null
        private val imageLogListeners: MutableList<ImageLogListener> = ArrayList()
        private val imageSensibleLogListeners: MutableList<ImageSensibleLogListener> = ArrayList()
        private val imageExceedLogListeners: MutableList<ImageExceedLogListener> = ArrayList()

        fun getInstance(): Context? {
            return sInstance
        }

        @Synchronized
        fun addImageLogListener(listener: ImageLogListener) {
            imageLogListeners.add(listener)
        }

        @Synchronized
        fun addImageSensibleLogListener(listener: ImageSensibleLogListener) {
            imageSensibleLogListeners.add(listener)
        }

        @Synchronized
        fun addImageExceedLogListener(listener: ImageExceedLogListener) {
            imageExceedLogListeners.add(listener)
        }

        @Synchronized
        fun removeImageLogListener(listener: ImageLogListener) {
            imageLogListeners.remove(listener)
        }

        @Synchronized
        fun removeSensibleImageLogListener(listener: ImageSensibleLogListener) {
            imageSensibleLogListeners.remove(listener)
        }

        @Synchronized
        fun removeImageExceedLogListener(listener: ImageExceedLogListener) {
            imageExceedLogListeners.remove(listener)
        }


        @Synchronized
        private fun notifyImageLog(isSucceed: Boolean, requestId: String?, jsonObject: JSONObject?) {
            imageLogListeners.forEach {
                it.onImageLoad(isSucceed, requestId, jsonObject)
            }
        }

        @Synchronized
        private fun notifySensibleImageLog(jsonObject: JSONObject?) {
            imageSensibleLogListeners.forEach {
                it.onSensibleCallback(jsonObject)
            }
        }

        @Synchronized
        private fun notifyExceedImageLog(jsonObject: JSONObject?) {
            imageExceedLogListeners.forEach {
                it.onExceedLimitCallback(jsonObject)
            }
        }
    }

    interface ImageLogListener {
        fun onImageLoad(isSucceed: Boolean, requestId: String?, jsonObject: JSONObject?)
    }

    interface ImageSensibleLogListener {
        fun onSensibleCallback(jsonObject: JSONObject?)
    }

    interface ImageExceedLogListener {
        fun onExceedLimitCallback(jsonObject: JSONObject?)
    }


    override fun onCreate() {
        super.onCreate()
        FLog.setMinimumLoggingLevel(FLog.VERBOSE)
        Fresco.initialize(this)
        TestFile.filePathType = TestFile.FILE_PATH_TYPE_SDCARD
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        sInstance = base
    }
}