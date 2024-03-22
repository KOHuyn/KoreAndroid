package com.kohuyn.core.permission

interface PermissionResultInvoke {
    fun onPermissionGranted(requestCode: Int?, isGranted: Boolean)
    fun isReplayValue(): Boolean = false
}