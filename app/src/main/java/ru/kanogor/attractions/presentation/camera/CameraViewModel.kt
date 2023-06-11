package ru.kanogor.attractions.presentation.camera

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kanogor.attractions.data.dataBase.DataBaseRepository
import ru.kanogor.attractions.entity.Photo
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

private const val FILENAME_FORMAT = "yyy-MM-dd-HH-mm-ss"

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {
    fun insert(photo: Photo) {
        viewModelScope.launch {
            dataBaseRepository.insert(photo)
        }
    }

    val photo = this.dataBaseRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
    private var imageCapture: ImageCapture? = null

    fun startCamera(
        view: PreviewView,
        executor: Executor,
        lifecycleOwner: LifecycleOwner,
        context: Context
    ) {

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(view.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        }, executor)
    }

    private val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())

    fun takePicture(
        view: ImageView,
        executor: Executor,
        context: Context,
        contentResolver: ContentResolver
    ) {
        val imageCapture = imageCapture ?: return

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()
        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri
                    Toast.makeText(
                        context,
                        "Photo saved $savedUri",
                        Toast.LENGTH_SHORT
                    ).show()

                    Glide.with(
                        view.context
                    )
                        .load(savedUri)
                        .centerCrop()
                        .into(view)

                    insert(
                        Photo(
                            uri = savedUri.toString(),
                            date = name
                        )
                    )
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        context,
                        "Photo unsaved: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    exception.printStackTrace()
                }
            }
        )
    }
}