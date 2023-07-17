package io.drdroid.camera_gallery.data.interfaces

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.MediaStore
import io.drdroid.camera_gallery.App
import io.drdroid.camera_gallery.data.models.Folder
import io.drdroid.camera_gallery.data.models.GalleryItem

interface MediaCall {

    suspend fun getFolders(): MutableList<Folder>


}