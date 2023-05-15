package com.ducpv.composeui.shared.utility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Created by pvduc9773 on 12/05/2023.
 */
object PermissionUtility {
    val runTrackingPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    ).toList()

    fun hasPermissions(context: Context, permissions: List<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun permissionsGranted(permissions: Map<String, Boolean>): Boolean {
        if (permissions.isEmpty()) return false
        for (permission in permissions) {
            if (permission.value) return false
        }
        return true
    }
}
