package io.drdroid.camera_gallery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import io.drdroid.camera_gallery.data.models.GalleryItem
import io.drdroid.camera_gallery.databinding.FragmentGalleryViewBinding
import io.drdroid.camera_gallery.ui.activities.Gallery
import io.drdroid.camera_gallery.ui.adapters.GalleryViewAdapter
import io.drdroid.camera_gallery.ui.base.BaseFragment

@AndroidEntryPoint
class GalleryViewFragment : BaseFragment() {

    private lateinit var bind: FragmentGalleryViewBinding

    private var position: Int = 0
    private lateinit var list: List<GalleryItem>

    private lateinit var viewPager: ViewPager2
    private lateinit var recyclerView: RecyclerView

    private lateinit var pAdapter: GalleryViewAdapter
    private lateinit var dotsIndicator: DotsIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        position = requireArguments().getInt("position")

        val itemType = object : TypeToken<List<GalleryItem>>() {}.type
        list = Gson().fromJson(requireArguments().getString("list"), itemType)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentGalleryViewBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as Gallery).showToolbar()
//        (activity as Gallery).supportActionBar?.let {
//            (activity as Gallery).supportActionBar!!.title = title
//        }

        viewPager = bind.viewPager
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        recyclerView = bind.recyclerView



        pAdapter = GalleryViewAdapter(requireContext(), list)
        viewPager.adapter = pAdapter
        viewPager.currentItem = position
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                pAdapter.releasePlayer()
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                this@GalleryViewFragment.position = position

                pAdapter.updatePlayer(list[position].path)

                (activity as Gallery).supportActionBar?.let {
                    (activity as Gallery).supportActionBar!!.title = list[position].name
                }
            }

//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//            }
        })

        dotsIndicator = bind.dotsIndicator
        dotsIndicator.attachTo(viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        pAdapter.releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}