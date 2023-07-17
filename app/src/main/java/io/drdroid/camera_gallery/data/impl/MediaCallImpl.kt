package io.drdroid.camera_gallery.data.impl

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.MediaStore
import io.drdroid.camera_gallery.App
import io.drdroid.camera_gallery.data.interfaces.MediaCall
import io.drdroid.camera_gallery.data.models.Folder
import io.drdroid.camera_gallery.data.models.GalleryItem

class MediaCallImpl : MediaCall {

    override suspend fun getFolders(): MutableList<Folder>  {
        val allFolder = mutableListOf<Folder>()

        getImageFolders(allFolder)
        getVideoFolders(allFolder)

        return allFolder
    }

    @SuppressLint("Range")
    private fun getImageFolders(allFolder: MutableList<Folder>) {
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.SIZE,
//            MediaStore.Images.Media.DURATION,
            MediaStore.Images.Media.MIME_TYPE,
        )
        val images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        val cur: Cursor = App.instance.contentResolver.query(
            images,
            projection,  // Which columns to return
            null,  // Which rows to return (all rows)
            null,  // Selection arguments (none)
            "$orderBy DESC" // Ordering
        )!!
        var imagePath: ArrayList<GalleryItem>?
        if (cur.moveToFirst()) {

            val bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            do {
                val name = cur.getString(bucketColumn)
                var folder: Folder
                if (allFolder.none { it.name == name }) {
                    folder = Folder(name = name)
                    allFolder.add(folder)
                } else {
                    folder = allFolder.find { it.name == name }!!
                }

                imagePath = folder.files

                imagePath.add(
                    GalleryItem(
                        path = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA)),
                        name = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)),
                        dateAdded = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)),
                        dateModified = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)),
                        size = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.SIZE)),
//                        duration = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DURATION)),
                        mimeType = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
                    )
                )
//                allFolder[allFolder.indexOf(folder)] = folder
                folder.files = imagePath
            } while (cur.moveToNext())
        }
        cur.close()
    }

    @SuppressLint("Range")
    private fun getVideoFolders(allFolder: MutableList<Folder>) {
        val projection = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.MIME_TYPE,
        )
        val images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        val cur: Cursor = App.instance.contentResolver.query(
            images, projection,  // Which columns to return
            null,  // Which rows to return (all rows)
            null,  // Selection arguments (none)
            "$orderBy DESC" // Ordering
        )!!
        var videoPath: ArrayList<GalleryItem>?
        if (cur.moveToFirst()) {

            val bucketColumn = cur.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            do {
                val name = cur.getString(bucketColumn)
                var folder: Folder
                if (allFolder.none { it.name == name }) {
                    folder = Folder(name = name)
                    allFolder.add(folder)
                } else {
                    folder = allFolder.find { it.name == name }!!
                }

                videoPath = folder.files

                videoPath.add(
                    GalleryItem(
                        path = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.DATA)),
                        name = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)),
                        dateAdded = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)),
                        dateModified = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)),
                        size = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.SIZE)),
                        duration = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.DURATION))
                            ?: "",
                        mimeType = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))
                    )
                )
                folder.files = videoPath
            } while (cur.moveToNext())
        }
        cur.close()
    }
}