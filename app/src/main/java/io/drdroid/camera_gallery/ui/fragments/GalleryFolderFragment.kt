package io.drdroid.camera_gallery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import io.drdroid.camera_gallery.data.models.GalleryItem
import io.drdroid.camera_gallery.databinding.FragmentGalleryBinding
import io.drdroid.camera_gallery.ui.activities.Gallery
import io.drdroid.camera_gallery.ui.adapters.GalleryFolderAdapter
import io.drdroid.camera_gallery.ui.base.BaseFragment
import io.drdroid.camera_gallery.utils.Common
import io.drdroid.camera_gallery.utils.Common.getPreferences

@AndroidEntryPoint
class GalleryFolderFragment : BaseFragment() {

    private lateinit var bind: FragmentGalleryBinding

    private lateinit var title: String
    private lateinit var medias: MutableList<GalleryItem>

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: StaggeredGridLayoutManager
    private lateinit var adapter: GalleryFolderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = requireArguments().getString("title")!!

        val itemType = object : TypeToken<List<GalleryItem>>() {}.type
        medias = Gson().fromJson(requireArguments().getString("list"), itemType)
        sortList(
            getPreferences().getInt(Common.PREFERENCES.FILTER_KEY, 0),
            getPreferences().getInt(Common.PREFERENCES.DIRECTION_KEY, 0)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentGalleryBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as Gallery).showToolbar()
        (activity as Gallery).supportActionBar?.let {
            (activity as Gallery).supportActionBar!!.title = title
        }

        recyclerView = bind.recyclerView

        manager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = manager

        adapter = GalleryFolderAdapter(requireContext(), medias, findNavController())
        recyclerView.adapter = adapter
    }

    private fun sortList(filter: Int, direction: Int) {
        when (filter) {
            0 -> {
                medias.sortBy { it.name }
            }

            1 -> {
                medias.sortBy { it.dateModified }
            }

            2 -> {
                medias.sortBy { it.size }
            }
        }
        if (direction == 1) {
            medias.reverse()
        }
        if (this::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }
}