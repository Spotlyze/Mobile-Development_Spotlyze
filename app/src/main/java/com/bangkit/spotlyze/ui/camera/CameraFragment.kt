package com.bangkit.spotlyze.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.ui.analyze.quiz.QuizActivity
import com.bangkit.spotlyze.utils.createCustomTempFile
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

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
//        setupViewModel()

    }

//    private fun setupViewModel() {
//        viewModel.result.observe(viewLifecycleOwner) { data ->
//            when (data) {
//                is Result.Error -> {
//                    Log.d("okhttp", "classify error: ${data.error}")
//                }
//                Result.Loading -> {
//
//                }
//                is Result.Success -> {
//                    val result = data.data.message
//                    Log.d("okhttp", "classify data: $result")
//                    val intent = Intent(requireActivity(), ResultActivity::class.java)
//                    intent.putExtra(EXTRA_RESULT, result.toString())
//                    requireActivity().startActivity(intent)
//                    requireActivity().finish()
//                }
//            }
//        }
//    }

    private fun setupAction() {
        switchCamera()
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
                    val intent = Intent(requireActivity(), QuizActivity::class.java)
                    intent.putExtra(EXTRA_RESULT, result.toString())
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                    Log.d("okhttp", "result: $result")
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

//    private fun classifyImage(imageUri: Uri) {
//        viewModel.classifySkin("Test aja", imageUri, requireActivity())
//    }

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
        private const val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA
        const val EXTRA_RESULT = "CameraX Result"
    }
}