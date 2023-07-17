package io.drdroid.camera_gallery.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.common.util.concurrent.ListenableFuture
import io.drdroid.camera_gallery.databinding.ActivityCameraBinding
import io.drdroid.camera_gallery.interfaces.PermissionListener
import io.drdroid.camera_gallery.ui.base.BaseActivity
import io.drdroid.camera_gallery.utils.Common
import io.drdroid.camera_gallery.utils.Utils
import io.drdroid.camera_gallery.utils.Utils.hasPermissions
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Camera : BaseActivity(), PermissionListener {

    companion object {
        val TAG: String = Camera::class.java.name
    }

    private lateinit var cameraList: Array<String>
    lateinit var bind: ActivityCameraBinding
    lateinit var cameraView: PreviewView

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraManager: CameraManager

    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService

//    private lateinit var

    private lateinit var takePicture: ImageButton
    private lateinit var switchCamera: ImageButton
    private lateinit var toGallery: ImageButton
    private lateinit var toggleFlash: ImageButton
    private var torchOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraList = cameraManager.cameraIdList

        bind = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(bind.root)

        cameraView = bind.cameraView

        switchCamera = bind.switchCamera
        switchCamera.setOnClickListener {
            switchCamera()
        }

        toGallery = bind.toGallery
        toGallery.setOnClickListener {
            startActivity(Intent(this@Camera, Gallery::class.java))
        }

        toggleFlash = bind.toggleFlash
        toggleFlash.setOnClickListener {
            toggleFlashOnOff()
        }

        takePicture = bind.takePicture
        takePicture.setOnClickListener {
            takePhoto()
        }
    }

    private fun toggleFlashOnOff() {
//        cameraManager.setTorchMode("", torchOn)
        Toast.makeText(this@Camera, "Feature not yet available", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        if (!hasPermissions(this@Camera, Common.PERMISSIONS.PERMISSION_CAMERA_ALL)) {
            super.getCameraPermissions(this@Camera)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        // listening for data from the camera
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // connecting a preview use case to the preview in the xml file.
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(cameraView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                // clear all the previous use cases first.
                cameraProvider.unbindAll()
                // binding the lifecycle of the camera to the lifecycle of the application.
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        imageCapture?.let {
            imgCaptureExecutor = Executors.newSingleThreadExecutor()
            //Create a storage location whose fileName is timestamped in milliseconds.
            val fileName = "JPEG_${System.currentTimeMillis()}.jpeg"
            val file = File(externalMediaDirs[0], fileName)

            // Save the image in the above file
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            /* pass in the details of where and how the image is taken.(arguments 1 and 2 of takePicture)
            pass in the details of what to do after an image is taken.(argument 3 of takePicture) */

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        animateFlash()
                        Log.i(TAG, "The image has been saved in ${file.toUri()}")
                        Utils.scanMedia(file)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            this@Camera,
                            "Error taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, "Error taking photo:$exception")
                    }

                })
        }
    }

    private fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            toggleFlash.visibility = View.GONE
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            toggleFlash.visibility = View.VISIBLE
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        // restart the camera
        startCamera()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun animateFlash() {
        bind.root.postDelayed({
            bind.root.foreground = ColorDrawable(Color.WHITE)
            bind.root.postDelayed({
                bind.root.foreground = null
            }, 50)
        }, 100)
    }

    override fun onPermissionResult(isGranted: Boolean, code: Int) {
        when (code) {
            Common.PERMISSIONS.PERMISSION_CAMERA_ALL_CODE -> {
                if (isGranted) {
                    startCamera()
                }
            }
        }
    }
}