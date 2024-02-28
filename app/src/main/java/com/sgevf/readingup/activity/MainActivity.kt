package com.sgevf.readingup.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sgevf.readingup.FloatingWindowService
import com.sgevf.readingup.Utils

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.checkFloatingWindowPermission(this)

    }

    override fun onResume() {
        super.onResume()
        if (Utils.commonROMPermissionCheck(this)) {
            startService(Intent(this, FloatingWindowService::class.java))
//            finish()
        }
    }
}