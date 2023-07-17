package io.drdroid.camera_gallery.data

import android.os.Environment
import io.drdroid.camera_gallery.App
import io.drdroid.camera_gallery.R
import java.io.File
import java.util.Arrays
import java.util.Locale

object FileManager {

    val TAG: String = FileManager::class.java.name

    private fun appDirectory(): File? {
        val fallBackDir = File(
            Environment.getExternalStorageDirectory(),
            String.format(Locale.getDefault(), "Android/%s", App.instance.getPackageName())
        )
        val dirs: Array<File> = App.instance.getExternalMediaDirs()
        val dir =
            Arrays.stream(dirs).filter { d: File -> d.name == App.instance.getPackageName() }
                .findFirst().orElse(fallBackDir)
        return File(dir, App.instance.resources.getString(R.string.app_name))
    }

    /**
     * The directory where all media will be saved
     *
     * @return the media directory of the app
     */
    fun getMediaDirectory(): File? {
        val mediaDir = File(appDirectory(), "media")
        if (mediaDir.exists()) {
            mediaDir.mkdirs()
        }
        return mediaDir
    }

//    fun getImageMediaFile(): File {
//
//    }
//
//    fun getVideoMediaFile(): File {
//
//    }
}