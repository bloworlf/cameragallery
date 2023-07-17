package io.drdroid.camera_gallery.utils

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import io.drdroid.camera_gallery.App
import java.io.File

object Utils {

    fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun View.hideKeyboard() {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun scanMedia(file: File) {
        try {
            if (file.name.endsWith("mp4")) {
                MediaScannerConnection.scanFile(
                    App.instance,
                    arrayOf<String>(file.path),
                    arrayOf<String>("video/mp4"),
                    null
                )
            } else {
                MediaScannerConnection.scanFile(
                    App.instance,
                    arrayOf<String>(file.path),
                    arrayOf<String>("image/jpg"),
                    null
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}