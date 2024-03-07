package com.sgevf.readingup.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter4.BaseQuickAdapter
import com.hjq.bar.OnTitleBarListener
import com.sgevf.readingup.BaseActivity
import com.sgevf.readingup.FloatingWindowService
import com.sgevf.readingup.R
import com.sgevf.readingup.Utils
import com.sgevf.readingup.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var mDataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.checkFloatingWindowPermission(this)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


//        if (Utils.commonROMPermissionCheck(this)) {
//            startService(Intent(this, FloatingWindowService::class.java))
//        }
        startActivity(Intent(this, TaskDetailActivity::class.java))
        finish()
    }
}