package io.drdroid.camera_gallery.data.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.drdroid.camera_gallery.data.models.Folder
import io.drdroid.camera_gallery.data.repo.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    val liveData: MutableLiveData<MutableList<Folder>> by lazy {
        MutableLiveData<MutableList<Folder>>()
    }

    fun getMedia() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = repo.getFolders()
            liveData.postValue(result)
        }
    }
}