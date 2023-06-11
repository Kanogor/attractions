package ru.kanogor.attractions.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kanogor.attractions.entity.Photo

@Database(entities = [Photo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

}