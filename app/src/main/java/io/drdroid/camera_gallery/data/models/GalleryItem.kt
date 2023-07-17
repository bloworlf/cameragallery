package io.drdroid.camera_gallery.data.models

class GalleryItem(
    val name: String,
    val path: String,
    val dateAdded: String,
    val dateModified: String,
    val size: String,
    val duration: String = "",
    val mimeType: String
)