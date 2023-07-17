package io.drdroid.camera_gallery.data.repo

import io.drdroid.camera_gallery.data.models.Folder

interface Repository {

    suspend fun getFolders(): MutableList<Folder>
}