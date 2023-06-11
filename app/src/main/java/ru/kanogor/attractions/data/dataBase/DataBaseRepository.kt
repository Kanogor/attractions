package ru.kanogor.attractions.data.dataBase

import ru.kanogor.attractions.entity.Photo
import javax.inject.Inject

class DataBaseRepository @Inject constructor(private val photoDao: PhotoDao) {

    fun getAll() = photoDao.getAll()

    suspend  fun insert(photo: Photo) = photoDao.insert(photo)

    suspend fun delete(uriToString: String) = photoDao.delete(uriToString)

    suspend fun clearAll(photo: Photo) = photoDao.clearAll(photo)

}