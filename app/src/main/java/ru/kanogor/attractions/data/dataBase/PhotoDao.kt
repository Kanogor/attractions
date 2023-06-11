package ru.kanogor.attractions.data.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kanogor.attractions.entity.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos")
   fun getAll(): Flow<List<Photo>>

    @Insert
   suspend fun insert(photo: Photo)

    @Query("DELETE FROM photos WHERE uri LIKE :uriToString")
    suspend fun delete(uriToString: String)

    @Delete
    suspend fun clearAll(photo: Photo)
}