package com.sgevf.readingup.screenshot

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
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder

class ScreenShotService : Service() {
    private val mediaProjectManager =
        getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val screenWidth = resources.displayMetrics.widthPixels
    private val screenHeight = resources.displayMetrics.heightPixels
    private val screenDensity = resources.displayMetrics.densityDpi
    private val imageReader =
        ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1)
    private val handlerThread = HandlerThread("ScreenShotService")
    private val handler = Handler(handlerThread.looper)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return ScreenShotBinder()
    }

    inner class ScreenShotBinder : Binder() {

        fun screenCapture(): Bitmap {
            if (mediaProjection == null) {
                mediaProjection = mediaProjectManager.getMediaProjection()
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

            val bitmap = formatImageToBitmap(imageReader.acquireLatestImage())
            return bitmap
        }

    }
    private fun formatImageToBitmap(image: Image) : Bitmap{
        val bWidth = image.width
        val bHeight = image.height
        val bBuffer = image.planes[0].buffer
        val bPixelStride = image.planes[0].pixelStride
        val bRowStride = image.planes[0].rowStride
        val bRowPadding = bRowStride - bPixelStride * bWidth
        val bitmap = Bitmap.createBitmap(bWidth + bRowPadding / bPixelStride, bHeight, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(bBuffer)
        image.close()
        return bitmap
    }


}