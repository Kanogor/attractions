package ru.kanogor.attractions.presentation.recyclerview

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kanogor.attractions.databinding.PhotoGalleryItemBinding
import ru.kanogor.attractions.entity.Photo

class PhotoGalleryAdapter(private val onClick: (Photo) -> Unit) :
    RecyclerView.Adapter<PhotoGalleryViewHolder>() {
    private var data: List<Photo> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Photo>) {
        this.data = data
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGalleryViewHolder {
        return PhotoGalleryViewHolder(
            PhotoGalleryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoGalleryViewHolder, position: Int) {
        val item = data.getOrNull(position)
        with(holder.binding) {
            item?.let {
                Glide.with(photoImage.context)
                    .load(Uri.parse(it.uri))
                    .into(photoImage)
            }
            dataText.text = item?.date
            root.setOnClickListener {
                item?.let {
                    onClick(item)
                }

            }
        }
    }

    override fun getItemCount() = data.size
}

class PhotoGalleryViewHolder(val binding: PhotoGalleryItemBinding) :
    RecyclerView.ViewHolder(binding.root)