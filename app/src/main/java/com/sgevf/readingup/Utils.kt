package com.sgevf.readingup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

    /**
     * 返回到手机主页
     */
    fun backToPhoneHome(context: Context) {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).first()?.let {
            val info = it.activityInfo
            val launcherIntent = Intent(Intent.ACTION_MAIN)
            launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            launcherIntent.setClassName(info.packageName, info.name)
            context.startActivity(launcherIntent)
        }
    }

    /**
     * 返回应用
     */
    fun backToAppHome() {
        
    }
}