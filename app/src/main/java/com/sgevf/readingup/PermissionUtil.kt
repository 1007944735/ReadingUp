package com.sgevf.readingup

import android.content.Context
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PermissionUtil {

    suspend fun permit(context: Context, vararg permissions: String): Boolean = suspendCoroutine {
        XXPermissions.with(context)
            .apply {
                permissions.forEach {
                    permission(it)
                }
            }
            .unchecked()
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    it.resume(allGranted)
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    super.onDenied(permissions, doNotAskAgain)
                    it.resume(false)
                }
            })
    }

    fun isGrant(context: Context, vararg permissions: String): Boolean {
        return XXPermissions.isGranted(context, permissions)
    }
}