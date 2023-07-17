package io.drdroid.camera_gallery.interfaces

interface PermissionListener {

    fun onPermissionResult(isGranted: Boolean, code: Int)
}