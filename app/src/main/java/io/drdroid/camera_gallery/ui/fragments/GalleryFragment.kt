package io.drdroid.camera_gallery.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.drdroid.camera_gallery.R
import io.drdroid.camera_gallery.data.models.Folder
import io.drdroid.camera_gallery.data.vm.GalleryViewModel
import io.drdroid.camera_gallery.databinding.FragmentGalleryBinding
import io.drdroid.camera_gallery.interfaces.PermissionListener
import io.drdroid.camera_gallery.ui.activities.Gallery
import io.drdroid.camera_gallery.ui.adapters.GalleryAdapter
import io.drdroid.camera_gallery.ui.base.BaseFragment
import io.drdroid.camera_gallery.ui.dialog.CustomDialog
import io.drdroid.camera_gallery.utils.Common
import io.drdroid.camera_gallery.utils.Common.getPreferences
import io.drdroid.camera_gallery.utils.GridSpacingItemDecoration
import io.drdroid.camera_gallery.utils.Utils
import io.drdroid.camera_gallery.utils.Utils.hideKeyboard
import xyz.teamgravity.imageradiobutton.GravityImageRadioButton
import xyz.teamgravity.imageradiobutton.GravityRadioGroup

@AndroidEntryPoint
class GalleryFragment : BaseFragment(), PermissionListener {

    private lateinit var bind: FragmentGalleryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: StaggeredGridLayoutManager
    private lateinit var adapter: GalleryAdapter

//    var allFolder: MutableList<Folder> = mutableListOf()

    private val viewModel: GalleryViewModel by viewModels()
//    var listImageByFolder: HashMap<String, ArrayList<String>> = HashMap()
//    var allVideoFolder: MutableList<String> = mutableListOf()
//    var listVideoByFolder: HashMap<String, ArrayList<String>> = HashMap()

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        if (!Utils.hasPermissions(requireContext(), Common.PERMISSIONS.PERMISSION_STORAGE)) {
            (requireActivity() as Gallery).getStoragePermission(this)
        } else {
            getFolders()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (!this::adapter.isInitialized) {
            return
        }
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                manager.spanCount = 5
                adapter.notifyDataSetChanged()
            }

//            Configuration.ORIENTATION_PORTRAIT -> {
//                manager.spanCount = 3
//                adapter.notifyDataSetChanged()
//            }

            else -> {
                manager.spanCount = 3
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getFolders() {
//        getImageFolders()
//        getVideoFolders()

//        allFolder = allFolder.sortedBy { it.name }.toMutableList()
//        println(allFolder)
//        adapter.list = allFolder
//        adapter.notifyDataSetChanged()

        viewModel.getMedia()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentGalleryBinding.inflate(inflater, container, false)
        return bind.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)
        searchView = (menu.findItem(R.id.search).actionView as SearchView)
        searchView!!.maxWidth = Integer.MAX_VALUE

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length <= 2) {
                    Toast.makeText(
                        this@GalleryFragment.requireContext(),
                        "Query string too short",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
//                filter(query)
                searchView!!.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> {
                val view: View = layoutInflater.inflate(R.layout.include_filter, null)
                val filter: GravityRadioGroup = view.findViewById(R.id.filter)
                val direction: GravityRadioGroup = view.findViewById(R.id.direction)

                val filterName: GravityImageRadioButton = view.findViewById(R.id.filterName)
                val filterDate: GravityImageRadioButton = view.findViewById(R.id.filterDate)
                val filterSize: GravityImageRadioButton = view.findViewById(R.id.filterSize)

                val filterAsc: GravityImageRadioButton = view.findViewById(R.id.filterAsc)
                val filterDesc: GravityImageRadioButton = view.findViewById(R.id.filterDesc)

                when (getPreferences().getInt(Common.PREFERENCES.FILTER_KEY, 0)) {
                    0 -> {
                        filterName.isChecked = true
                    }

                    1 -> {
                        filterDate.isChecked = true
                    }

                    2 -> {
                        filterSize.isChecked = true
                    }
                }
                when (getPreferences().getInt(Common.PREFERENCES.DIRECTION_KEY, 0)) {
                    0 -> {
                        filterAsc.isChecked = true
                    }

                    1 -> {
                        filterDesc.isChecked = true
                    }
                }

                CustomDialog(requireContext(), true)
                    .includeView(view)
                    .setPositive("Done") {
                        val f = when (filter.checkedRadioButtonId()) {
                            filterName.id -> {
                                getPreferences().edit().putInt(Common.PREFERENCES.FILTER_KEY, 0)
                                    .apply()
                                0
                            }

                            filterDate.id -> {
                                getPreferences().edit().putInt(Common.PREFERENCES.FILTER_KEY, 1)
                                    .apply()
                                1
                            }

                            filterSize.id -> {
                                getPreferences().edit().putInt(Common.PREFERENCES.FILTER_KEY, 2)
                                    .apply()
                                2
                            }

                            else -> {
                                0
                            }
                        }
                        val d = when (direction.checkedRadioButtonId()) {
                            filterAsc.id -> {
                                getPreferences().edit().putInt(Common.PREFERENCES.DIRECTION_KEY, 0)
                                    .apply()
                                0
                            }

                            filterDesc.id -> {
                                getPreferences().edit().putInt(Common.PREFERENCES.DIRECTION_KEY, 1)
                                    .apply()
                                1
                            }

                            else -> {
                                0
                            }
                        }

                        sortList(adapter.list, f, d)
                    }
                    .setNegative("Cancel") {}
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortList(folders: MutableList<Folder>, filter: Int, direction: Int) {
        when (filter) {
            0 -> {
                folders.sortBy { it.name }
            }

            1 -> {
                folders.sortBy { it.files.maxOf { g -> g.dateModified } }
            }

            2 -> {
                folders.sortBy { it.files.sumOf { g -> g.size.toLong() } }
            }
        }
        if (direction == 1) {
            folders.reverse()
        }
        if (this::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = bind.recyclerView
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(
                GridSpacingItemDecoration(
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        5
                    } else {
                        3
                    }, 10, true
                )
            )
        }

        manager = StaggeredGridLayoutManager(
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                5
            } else {
                3
            }, StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.layoutManager = manager

//        adapter = GalleryAdapter(requireContext(), mutableListOf(), findNavController())
//        recyclerView.adapter = adapter

        viewModel.liveData.observe(viewLifecycleOwner) {
            sortList(
                it,
                getPreferences().getInt(Common.PREFERENCES.FILTER_KEY, 0),
                getPreferences().getInt(Common.PREFERENCES.DIRECTION_KEY, 0)
            )

            adapter = GalleryAdapter(requireContext(), it, findNavController())
            recyclerView.adapter = adapter
        }
    }

    override fun onPermissionResult(isGranted: Boolean, code: Int) {
        when (code) {
            Common.PERMISSIONS.PERMISSION_STORAGE_CODE -> {
                if (isGranted) {
                    getFolders()
                } else {
                    requireActivity().onNavigateUp()
                }
            }
        }
    }


}