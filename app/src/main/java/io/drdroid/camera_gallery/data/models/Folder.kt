package io.drdroid.camera_gallery.data.models

import java.util.ArrayList

class Folder(
    val id: Long = System.currentTimeMillis(),
    var name: String? = "",
    var path: String = "",
    var files: ArrayList<GalleryItem> = arrayListOf()
)