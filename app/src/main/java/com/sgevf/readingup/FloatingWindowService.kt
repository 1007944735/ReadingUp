package com.sgevf.readingup

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.sgevf.readingup.activity.MainActivity
import com.sgevf.readingup.screenshot.ScreenShotService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FloatingWindowService: Service() {
    private var screenShotService: ScreenShotService? = null
    private var resultCode: Int = -1
    private var resultData: Intent? = null
    private var floatingWindow: View? = null

    private val wm by lazy { getSystemService(WINDOW_SERVICE) as WindowManager }

    private val con = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            p1?.let {
                screenShotService = (it as ScreenShotService.ScreenShotBinder).getService()
                startScreenShotService()

                Build.VERSION_CODES.O
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            screenShotService = null
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        showWindow()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            resultCode = it.getIntExtra("resultCode", -1)
            resultData = it.getParcelableExtra("resultData")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showWindow() {
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        val layoutParams = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

            format = PixelFormat.RGBA_8888

            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            //位置大小设置
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.START or Gravity.TOP
        }

        floatingWindow = LayoutInflater.from(this).inflate(R.layout.layout_functional_floating_window, null)
        floatingWindow?.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            })
            startScreenShotService()

        }
        wm.addView(floatingWindow, layoutParams)
    }

    private fun hideWindow() {
        floatingWindow?.let {
            wm.removeView(it)
            floatingWindow = null
        }
    }

    private fun startScreenShotService() {
        if (screenShotService == null) {
            val intent = Intent(this, ScreenShotService::class.java)
            intent.putExtra("resultCode", resultCode)
            intent.putExtra("resultData", resultData)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }
            bindService(intent, con, Service.BIND_AUTO_CREATE)
        } else {
            hideWindow()
            CoroutineScope(Dispatchers.IO).launch {
                screenShotService!!.screenCapture()?.let {
                    withContext(Dispatchers.Main) {
                        it
                        Utils.backToAppHome()
                    }
                }
            }
        }
    }


}