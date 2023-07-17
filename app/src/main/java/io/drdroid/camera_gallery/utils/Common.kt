package io.drdroid.camera_gallery.utils

import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import io.drdroid.camera_gallery.App

object Common {

    object PERMISSIONS {
        const val PERMISSION_NOTIFICATION: String = android.Manifest.permission.POST_NOTIFICATIONS

        val PERMISSION_STORAGE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        const val PERMISSION_STORAGE_CODE: Int = 121

        private const val PERMISSION_CAMERA: String = android.Manifest.permission.CAMERA
        private const val PERMISSION_MICROPHONE: String = android.Manifest.permission.RECORD_AUDIO

        val PERMISSION_CAMERA_ALL = arrayOf(
            PERMISSION_CAMERA,
            PERMISSION_MICROPHONE
        )
        const val PERMISSION_CAMERA_ALL_CODE: Int = 111

    }

    fun getPreferences(): SharedPreferences {
        return PREFERENCES.preferences
    }

    object PREFERENCES {
        const val FILTER_KEY: String = "filter"
        const val DIRECTION_KEY: String = "direction"
        val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.instance)
    }

    object ARGUMENTS {

        const val ARG_REVEAL_SETTINGS: String = "ARG_REVEAL_SETTINGS"
    }
}