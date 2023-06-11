package ru.kanogor.attractions.presentation.openPhoto

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.kanogor.attractions.R
import ru.kanogor.attractions.databinding.FragmentOpenPhotoBinding
import ru.kanogor.attractions.presentation.photoGallery.PhotoGalleryViewModel

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class OpenPhotoFragment : Fragment() {

    private var _binding: FragmentOpenPhotoBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private val viewModel: PhotoGalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(binding.bigPhoto.context)
            .load(Uri.parse(param1))
            .into(binding.bigPhoto)

        binding.deleteButton.setOnClickListener {
            findNavController().navigate(R.id.action_openPhotoFragment_to_PhotoGallery)
            viewModel.delete(param1!!)
        }
    }
}