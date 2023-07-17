package io.drdroid.camera_gallery.data.impl

import io.drdroid.camera_gallery.data.interfaces.MediaCall
import io.drdroid.camera_gallery.data.models.Folder
import io.drdroid.camera_gallery.data.repo.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    val mediaCall: MediaCall
) : Repository {
    override suspend fun getFolders(): MutableList<Folder> = mediaCall.getFolders()
}