package com.bangkit.spotlyze.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.spotlyze.ui.CameraViewModelFactory
import com.prayatna.spotlyze.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val factory: CameraViewModelFactory by lazy {
        CameraViewModelFactory.getInstance(requireActivity())
    }
    private val cameraViewModel: CameraViewModel by viewModels {
        factory
    }

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

        binding.switchCamera.setOnClickListener {
            cameraViewModel.switchCamera(viewLifecycleOwner, binding.previewCamera)
        }

        binding.imageGallery.setOnClickListener {
            startGallery()
        }
    }

    override fun onResume() {
        super.onResume()
        cameraViewModel.startCamera(viewLifecycleOwner, binding.previewCamera)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = CameraFragment::class.java.simpleName
        private const val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA
    }
}