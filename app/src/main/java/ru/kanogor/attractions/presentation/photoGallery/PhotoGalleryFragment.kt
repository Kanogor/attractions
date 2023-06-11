package ru.kanogor.attractions.presentation.photoGallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kanogor.attractions.R
import ru.kanogor.attractions.databinding.FragmentPhotoGalleryBinding
import ru.kanogor.attractions.entity.Photo
import ru.kanogor.attractions.presentation.recyclerview.PhotoGalleryAdapter

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null

    private val viewModel: PhotoGalleryViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoGalleryAdapter = PhotoGalleryAdapter { photo -> onItemClick(photo) }

        binding.recyclerView.adapter = photoGalleryAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        binding.buttonToCamera.setOnClickListener {
            findNavController().navigate(R.id.action_PhotoGallery_to_Camera)
        }
        binding.buttonToMap.setOnClickListener {
            findNavController().navigate(R.id.action_PhotoGallery_to_MapsFragment)
        }
        binding.buttonClear.setOnClickListener {
            viewModel.clearDatabase()
        }

        viewModel.photo.onEach {
            photoGalleryAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun onItemClick(photo: Photo) {
        val bundle = Bundle().apply {
            putString(ARG_PARAM1, photo.uri)
        }
        findNavController().navigate(R.id.action_PhotoGallery_to_openPhotoFragment, args = bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}