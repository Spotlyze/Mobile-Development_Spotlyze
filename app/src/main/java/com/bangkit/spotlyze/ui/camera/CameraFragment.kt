package com.bangkit.spotlyze.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.ui.SkinViewModelFactory
import com.bangkit.spotlyze.ui.classificationResult.ResultActivity
import com.bangkit.spotlyze.utils.createCustomTempFile
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CameraViewModel by viewModels{
        SkinViewModelFactory.getInstance(requireActivity())
    }

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireActivity(), "Permission request granted", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(requireActivity(), "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            Log.d(TAG, "Selected URI: $it")
        }
    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            REQUIRED_PERMISSIONS
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }

        setupAction()
        setupViewModel()

    }

    private fun setupViewModel() {
        viewModel.result.observe(viewLifecycleOwner) { data ->
            when (data) {
                is Result.Error -> {
                    Log.d("okhttp", "classify error: ${data.error}")
                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    val result = data.data.message
                    Log.d("okhttp", "classify data: $result")
                }
            }
        }
    }

    private fun setupAction() {
        switchCamera()
        gallery()
        takePicture()
    }

    private fun switchCamera() {
        binding.switchCamera.setOnClickListener {
            setupSwitchCamera()
        }
    }

    private fun takePicture() {
        binding.imageCapture.setOnClickListener {
            setupTakePicture()
        }
    }

    private fun gallery() {
        binding.imageGallery.setOnClickListener {
            startGallery()
        }
    }


    private fun setupTakePicture() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(requireContext().applicationContext)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val result = outputFileResults.savedUri!!
                    classifyImage(result)
                    Log.d("okhttp", "result: $result")
                    val intent = Intent(requireActivity(), ResultActivity::class.java)
                    intent.putExtra(EXTRA_RESULT, result.toString())
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("resultBaby", "onError: ${exception.message}", exception)
                    Message.toast(
                        requireActivity(),
                        resources.getString(R.string.failed_to_take_picture)
                        )
                }
            }
        )
    }

    private fun classifyImage(imageUri: Uri) {
        viewModel.classifySkin("Test aja", imageUri, requireActivity())
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build().also {
                    it.surfaceProvider = binding.previewCamera.surfaceProvider
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    requireActivity(),
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CX Err", e.message.toString())
                Result.Error(e.message.toString())
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupSwitchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }

        startCamera()
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = CameraFragment::class.java.simpleName
        private const val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA
        const val EXTRA_RESULT = "CameraX Result"
    }
}