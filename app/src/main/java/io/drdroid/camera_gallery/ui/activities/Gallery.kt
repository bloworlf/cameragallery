package io.drdroid.camera_gallery.ui.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import io.drdroid.camera_gallery.R
import io.drdroid.camera_gallery.databinding.ActivityGalleryBinding
import io.drdroid.camera_gallery.ui.base.BaseActivity

@AndroidEntryPoint
class Gallery : BaseActivity() {

    private lateinit var bind: ActivityGalleryBinding

    private lateinit var toolbar: Toolbar
    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(bind.root)

        toolbar = bind.toolbar
        setSupportActionBar(toolbar)

        controller = findNavController(R.id.fragment)
        val navConfig = AppBarConfiguration(
            setOf(R.id.galleryFragment)
        )

        controller.setGraph(R.navigation.navigation_gallery, intent.extras)

        setupActionBarWithNavController(controller, navConfig)
    }

    override fun onStart() {
        super.onStart()

//        if (!Utils.hasPermissions(this@Gallery, Common.PERMISSIONS.PERMISSION_STORAGE)) {
//            super.getStoragePermission(this@Gallery)
//        } else {
//            startGallery()
//        }
    }

    private fun startGallery() {
        //
    }

    override fun onSupportNavigateUp(): Boolean {
        return controller.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (!onSupportNavigateUp()) {
            super.onBackPressed()
        }
    }

    fun showToolbar() {
        if (this::bind.isInitialized) {
            bind.appbar.setExpanded(true, true)
        }
    }

//    override fun onPermissionResult(isGranted: Boolean, code: Int) {
//        //
//    }
}