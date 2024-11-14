package com.bangkit.spotlyze.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.prayatna.spotlyze.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val EXTRA_CAMERAX_IMAGE = "CameraX"
        private const val CAMERA_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (allPermissionGranted()) {
            startCamera()
        } else {
            requireActivity().requestPermissions(REQUIRED_PERMISSIONS, CAMERA_RESULT)
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun startCamera() {
        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = binding.previewCamera.surfaceProvider
                }
            val imageCapture = ImageCapture.Builder()
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    requireActivity(),
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(requireActivity(), "Cannot launch camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}