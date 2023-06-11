package ru.kanogor.attractions.presentation.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.kanogor.attractions.R
import ru.kanogor.attractions.databinding.FragmentCameraBinding
import java.util.concurrent.Executor

@AndroidEntryPoint
class CameraFragment : Fragment() {

    private val viewModel: CameraViewModel by viewModels()

    private lateinit var executor: Executor
    private var _binding: FragmentCameraBinding? = null

    private val binding get() = _binding!!

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) viewModel.startCamera(
                executor = executor,
                view = binding.viewFinder,
                lifecycleOwner = viewLifecycleOwner,
                context = requireContext().applicationContext
            )
            else {
                Toast.makeText(context, "Permission is not granted", Toast.LENGTH_SHORT).show()
            }
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

        executor = ContextCompat.getMainExecutor(requireContext().applicationContext)

        if (REQUEST_PERMISSION.all { permission ->
                shouldShowRequestPermissionRationale(permission)
            }
        ) {
            Toast.makeText(context, "You can't use this app without Camera.", Toast.LENGTH_SHORT)
                .show()
            checkPermission()
        } else checkPermission()

        viewModel.startCamera(
            executor = executor,
            view = binding.viewFinder,
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext().applicationContext
        )

        binding.buttonTakePicture.setOnClickListener {
            viewModel.takePicture(
                view = binding.photoPreviewButton,
                executor = executor,
                context = requireContext().applicationContext,
                contentResolver = requireActivity().contentResolver
            )
        }

        binding.photoPreviewButton.setOnClickListener {
            findNavController().navigate(R.id.action_Camera_to_PhotoGallery)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun checkPermission() {
        val isAllGranted = REQUEST_PERMISSION.all { permissions ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permissions
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted) {
            Toast.makeText(context, "Permission is Granted", Toast.LENGTH_SHORT).show()
        } else {
            launcher.launch(REQUEST_PERMISSION)
        }
    }

    companion object {
        private val REQUEST_PERMISSION: Array<String> = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}