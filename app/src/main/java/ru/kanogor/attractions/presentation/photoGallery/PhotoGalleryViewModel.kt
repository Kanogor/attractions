package ru.kanogor.attractions.presentation.photoGallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kanogor.attractions.data.dataBase.DataBaseRepository
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(private val dataBaseRepository: DataBaseRepository) : ViewModel() {

    fun delete(uri: String) {
        viewModelScope.launch {
            dataBaseRepository.delete(uri)
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            photo.value.forEach {
                dataBaseRepository.clearAll(it)
            }
        }
    }

    val photo = this.dataBaseRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}