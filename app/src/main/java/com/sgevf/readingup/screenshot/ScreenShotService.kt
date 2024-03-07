package com.sgevf.readingup.screenshot

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sgevf.readingup.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ScreenShotService : Service() {
    private val mediaProjectManager by lazy { getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager }
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val screenWidth by lazy { resources.displayMetrics.widthPixels }
    private val screenHeight by lazy { resources.displayMetrics.heightPixels }
    private val screenDensity by lazy { resources.displayMetrics.densityDpi }
    private val imageReader by lazy {
        ImageReader.newInstance(
            screenWidth,
            screenHeight,
            PixelFormat.RGBA_8888,
            1
        )
    }
    private val handlerThread = HandlerThread("ScreenShotService").apply { start() }
    private val handler = Handler(handlerThread.looper)
    private var resultCode = -1
    private var resultData: Intent? = null

    private val binder = ScreenShotBinder()

    private val FORE_SERVICE_CHANNEL_ID = "screen_shot"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                FORE_SERVICE_CHANNEL_ID,
                "ScreenShotService",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val channelManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channelManager.createNotificationChannel(channel)
        }
        startForeground(
            1, NotificationCompat.Builder(this, FORE_SERVICE_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("前台服务")
                .setContentText("正在运行").build()
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        intent?.let {
            resultCode = it.getIntExtra("resultCode", -1)
            resultData = it.getParcelableExtra("resultData")
        }
        return binder
    }

    inner class ScreenShotBinder : Binder() {

        fun getService() = this@ScreenShotService

    }

    suspend fun screenCapture(): Bitmap? = withContext(Dispatchers.IO) {
        if (resultCode != -1 || resultData == null) return@withContext null
        if (mediaProjection == null) {
            mediaProjection = mediaProjectManager.getMediaProjection(resultCode, resultData!!)
        }
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "virtualDisplay",
            screenWidth,
            screenHeight,
            screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface,
            null, handler
        )
        delay(1000)
        virtualDisplay?.release()
        virtualDisplay = null;
        return@withContext formatImageToBitmap(imageReader.acquireLatestImage())
    }

    private fun formatImageToBitmap(image: Image): Bitmap {
        val bWidth = image.width
        val bHeight = image.height
        val bBuffer = image.planes[0].buffer
        val bPixelStride = image.planes[0].pixelStride
        val bRowStride = image.planes[0].rowStride
        val bRowPadding = bRowStride - bPixelStride * bWidth
        val bitmap = Bitmap.createBitmap(
            bWidth + bRowPadding / bPixelStride,
            bHeight,
            Bitmap.Config.ARGB_8888
        )
        bitmap.copyPixelsFromBuffer(bBuffer)
        image.close()
        return bitmap
    }


}