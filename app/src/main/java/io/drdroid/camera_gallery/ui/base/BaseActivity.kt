package io.drdroid.camera_gallery.ui.base

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import io.drdroid.camera_gallery.interfaces.PermissionListener
import io.drdroid.camera_gallery.utils.Common

abstract class BaseActivity : AppCompatActivity() {

    private val storagePermissionContract: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
//            if (perms.containsValue(false)) {
//                //permissions denied
//            } else {
//                //permissions granted
//            }
            permissionListener.onPermissionResult(
                !perms.containsValue(false),
                Common.PERMISSIONS.PERMISSION_STORAGE_CODE
            )
        }

    private lateinit var permissionListener: PermissionListener
    private val cameraPermissionContract: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
//            if (perms.containsValue(false)) {
//                //permission(s) denied
//            } else {
//                //permissions granted
//                cameraPermissionListener.
//            }
            permissionListener.onPermissionResult(
                !perms.containsValue(false),
                Common.PERMISSIONS.PERMISSION_CAMERA_ALL_CODE
            )
        }

    private val notificationPermissionContract: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
            } else {
//            // Explain to the user that the feature is unavailable because the
//            // features requires a permission that the user has denied. At the
//            // same time, respect the user's decision. Don't link to system
//            // settings in an effort to convince the user to change their
//            // decision.
            }
        }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    fun getNotificationPermission() {
//        notificationPermissionContract.launch(Common.PERMISSIONS.PERMISSION_NOTIFICATION)
//    }

    fun getCameraPermissions(listener: PermissionListener) {
        permissionListener = listener
        cameraPermissionContract.launch(Common.PERMISSIONS.PERMISSION_CAMERA_ALL)
    }

    fun getStoragePermission(listener: PermissionListener) {
        permissionListener = listener
        storagePermissionContract.launch(Common.PERMISSIONS.PERMISSION_STORAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (hasPermissions(this, arrayOf(Common.PERMISSIONS.PERMISSION_NOTIFICATION))) {
////                settingsPrefs().edit()
////                    .putBoolean(resources.getString(R.string._notifications), true).apply()
//            } else {
//                notificationPermissionContract.launch(Common.PERMISSIONS.PERMISSION_NOTIFICATION)
////                settingsPrefs().edit()
////                    .putBoolean(resources.getString(R.string._notifications), false).apply()
//            }
//        }
    }
}