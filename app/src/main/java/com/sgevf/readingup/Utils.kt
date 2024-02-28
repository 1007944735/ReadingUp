package com.sgevf.readingup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

object Utils {

    const val REQUEST_FLOAT_CODE = 1001

    /**
     * 检查悬浮窗权限
     */
    fun checkFloatingWindowPermission(context: Activity) :Boolean{
        if (commonROMPermissionCheck(context)) {
            return true
        } else {
            context.startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:${context.packageName}")
            }, REQUEST_FLOAT_CODE)
            return false
        }
    }

    /**
     * 检查悬浮窗权限是否开启
     */

    fun commonROMPermissionCheck(context: Context): Boolean {
        var result = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = Settings.canDrawOverlays(context)
        }
        return result
    }
}