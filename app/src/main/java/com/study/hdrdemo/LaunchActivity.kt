package com.study.hdrdemo

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Switch
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.study.hdrdemo.utils.ZLogUtil
import java.io.File

class LaunchActivity : AppCompatActivity(), View.OnClickListener {

    private var mIntent: Intent? = null

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val REQUEST_PERMISSION_CODE = 1 //请求状态码


    var mStabilityTestButton: Button? = null
    var mMonkeyCheckButton: Button? = null
    var mScenceTestButton: Button? = null
    var mPerformanceButton: Button? = null
    var mResolutionTestButton: Button? = null
    var mColorTestButton: Button? = null
    var mAutoCaseSwitch: Switch? = null
    var mRadioBtnP0: RadioButton? = null
    var mNewCaseButton: Button? = null


    private val  TAG = "TTImage_LActivity"
    private val TAG_E = "TTImage_Error"


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        val filePath =
            applicationContext.getExternalFilesDir(null)!!.absolutePath + File.separator + "sdkAutoDemo" + File.separator
//        ZLogUtil.println(
//            TAG,
//            "BDFresco SDK: " + BuildConfig.BDFRESCO_VERSION
//        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

//        if (checkPermission(10001)) {
//            Log.e(TAG_E, "checkPermission fail !!!")
//        }

        //初始化按钮资源
        initBtn()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_PERMISSION_CODE
                )
            }
        }


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (applicationContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /**
         * @methodsName: onCreateOptionsMenu
         * @description: 添加菜单项
         */
        menuInflater.inflate(R.menu.moudle, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /**
         * @methodsName: onOptionsItemSelected
         * @description: 菜单点击回调
         */
        when (item.itemId) {
            R.id.LoadCases -> {
                return true
            }
            R.id.showLog -> {
                return true
            }
            R.id.moreSettings -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initBtn(){
        /**
         * @methodsName: initBtn
         * @description: 初始化按钮资源
         */

        mStabilityTestButton = findViewById<View>(R.id.stabilitytest) as Button?
        mStabilityTestButton!!.setOnClickListener(this)
        mMonkeyCheckButton = findViewById<View>(R.id.monkeytest) as Button?
        mMonkeyCheckButton!!.setOnClickListener(this)
        mScenceTestButton = findViewById<View>(R.id.scencetest) as Button?
        mScenceTestButton!!.setOnClickListener(this)
        mPerformanceButton = findViewById<View>(R.id.performtest) as Button?
        mPerformanceButton!!.setOnClickListener(this)
        mPerformanceButton = findViewById<View>(R.id.trackingtest) as Button?
        mPerformanceButton!!.setOnClickListener(this)
        mResolutionTestButton = findViewById<View>(R.id.resolutiontest) as Button?
        mResolutionTestButton!!.setOnClickListener(this)
        mColorTestButton = findViewById<View>(R.id.colortest) as Button?
        mColorTestButton!!.setOnClickListener(this)
        mNewCaseButton=  findViewById<View>(R.id.new_case) as Button?
        mNewCaseButton!!.setOnClickListener(this)
        mAutoCaseSwitch = findViewById<View>(R.id.autoTestSwitch) as Switch?
        mAutoCaseSwitch?.isChecked = true
        mRadioBtnP0 = findViewById(R.id.radio_p0)
        mRadioBtnP0?.isChecked = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        /**
         * @methodsName: onRequestPermissionsResult
         * @description: 运行时权限申请回调
         */
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (i in permissions.indices) {
//                ZLogUtil.i(
//                    TAG,
//                    "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]
//                )
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.new_case -> {
                ZLogUtil.i(TAG, "choose stabilitytest test mode")
//                mIntent = Intent(this, ShowBugActivity::class.java)
                mIntent = Intent(this, ShowSDRActivity::class.java)
//                mIntent = Intent(this, ShowHDRActivity::class.java)
                startActivity(mIntent)
            }
        }
    }

}
