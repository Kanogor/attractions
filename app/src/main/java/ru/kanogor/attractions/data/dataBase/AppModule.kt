package ru.kanogor.attractions.data.dataBase

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext app:Context) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "Photos_database"
    ).build()

    @Singleton
    @Provides
    fun providePhotoDao(db: AppDatabase) = db.photoDao()

}