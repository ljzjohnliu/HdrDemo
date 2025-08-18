package com.study.hdrdemo.utils

import android.os.Environment
import android.util.Log
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object ZLogUtil {
    /**
     * @version: V1.0
     * @author: zhujun.junzzy
     * @className: ZLogUtil
     * @packageName: android.ss.com.livesdkdemo.util
     * @description: 本地写日志类，将log写到sdcard和控制台
     * @date: 2020-04-09
     */
    private var basePath =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + "sdkAutoDemo" + File.separator
    private var fileName: String? = null
    private val keyLogFile: String? = null
    private var isAppend = false
    private val sdf = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
    private const val DEBUG = true
    fun setLocalPath(filePath: String) {
        /**
         * @methodsName: setLocalPath
         * @description: 设置日志保存路径
         */
        basePath = filePath
    }

    fun getFilePath() :String {
        val file = File(basePath + fileName)
        return file.absolutePath
    }

    //write debug log
    fun d(TAG: String, message: String) {
        if (DEBUG) {
            val logStr = createMessage(message)
            writeLogtoSDCard("Debug", TAG, logStr)
            Log.d(TAG, logStr)
        }
    }

    //write error log
    fun e(TAG: String, message: String) {
        if (DEBUG) {
            val logStr = createMessage(message)
            writeLogtoSDCard("Error", TAG, logStr)
            Log.e(TAG, logStr)
        }
    }

    //write info log
    fun i(TAG: String, message: String) {
        if (DEBUG) {
            val logStr = createMessage(message)
            writeLogtoSDCard("Info", TAG, logStr)
            Log.i(TAG, logStr)
        }
    }

    //write verbose log
    fun v(TAG: String, message: String) {
        if (DEBUG) {
            val logStr = createMessage(message)
            writeLogtoSDCard("Verbose", TAG, logStr)
            Log.v(TAG, logStr)
        }
    }

    //write warnning log
    fun w(TAG: String, message: String) {
        if (DEBUG) {
            val logStr = createMessage(message)
            writeLogtoSDCard("Warn", TAG, logStr)
            Log.e(TAG, logStr)
        }
    }

    //just print the message
    fun println(TAG: String, message: String) {
        if (DEBUG) {
            writeLogtoSDCard("println", TAG, message)
            Log.println(Log.INFO, TAG, message)
        }
    }

    //record the keylog to sdcard
    fun keyLog(TAG: String, message: String) {
        if (DEBUG) {
            writeLogtoSDCard("keyLog", TAG, message)
            Log.d(TAG, message)
        }
    }

    private fun createMessage(rawMessage: String): String {
        /**
         * @methodsName: createMessage
         * @description: 获取有类名与方法名的logString
         * @param String rawMessage
         * @return 类名+方法名串
         * Throwable().getStackTrace()获取的是程序运行的堆栈信息，也就是程序运行到此处的流程，以及所有方法的信息
         * 这里我们为什么取2呢？0是代表createMessage方法信息，1是代表上一层方法的信息，这里我们
         * 取它是上两层方法的信息
         */
        val stackTraceElement = Throwable().stackTrace[2]
        val fullClassName = stackTraceElement.className
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        return className + "." + stackTraceElement.methodName + "(): " + rawMessage
    }

    private fun writeLogtoSDCard(type: String, mTag: String, context: String) {
        /**
         * @methodsName: writeLog2SDCard
         * @description: 将日志写到sdcard里。程序每次运行创建一个新的txt，以时间为文件名
         * @param type    log的类型
         * @param context log的内容
         */
        var context = context
        if (fileName == null) fileName = sdf.format(Date(System.currentTimeMillis())) + ".txt"
        val file: File
        if (type == "println") {
            //printLn的日志不显示其他信息
            file = File(basePath + fileName)
        } else if (type == "keyLog") {
            //keyLog的日志写到keyLogFile里
            file = File(basePath + "live_event_key.log")
            val dateStr = sdf.format(Date(System.currentTimeMillis()))
            context = "$dateStr<$mTag>$context"
        } else {
            //verbose，info,debug,warnning,error的log写入到fileName里
            file = File(basePath + fileName)
            val dateStr = sdf.format(Date(System.currentTimeMillis()))
            context = "[$type] $dateStr<$mTag>==$context"
        }
        try {
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val bufferedWriter =
                BufferedWriter(OutputStreamWriter(FileOutputStream(file, isAppend)))
            bufferedWriter.write(context)
            bufferedWriter.newLine()
            bufferedWriter.flush()
            bufferedWriter.close()
            if (!isAppend) {
                isAppend = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readLogFromSDCard(file: String?): String {
        /**
         * @methodsName: readLogFromSDCard
         * @description: 从sdcard中读取case
         * @param file    日志的路径，为null时读取默认路径的日志
         */
        var file = file
        var resultLog = ""
        if (file == null) {
            file = basePath + fileName
            if (fileName == null) {
                return resultLog
            }
        } else {
            file = "$file.txt"
        }
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader(file))
            // The first way of reading the file
            println("Reading the file using readLine() method: ")
            var contentLine = br.readLine()
            while (contentLine != null) {
                resultLog += contentLine
                resultLog += '\n'
                contentLine = br.readLine()
            }
            return resultLog
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                br?.close()
            } catch (e: IOException) {
                println("Error in closing the BufferedReader")
            }
        }
        return resultLog
    }
}