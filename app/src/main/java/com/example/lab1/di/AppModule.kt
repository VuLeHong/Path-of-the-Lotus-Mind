package com.example.lab1.di

import android.content.Context
import androidx.room.Room
import com.example.lab1.data.local.AppDatabase
import com.example.lab1.data.local.dao.CharacterDao
import com.example.lab1.data.local.dao.SessionDao
import com.example.lab1.data.local.dao.InventoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "lab_db"
        ).build()
    }

    @Provides
    fun provideCharacterDao(db: AppDatabase): CharacterDao {
        return db.characterDao()
    }

    @Provides
    fun provideSessionDao(db: AppDatabase): SessionDao {
        return db.sessionDao()
    }

    @Provides
    fun provideInventoryDao(db: AppDatabase): InventoryDao {
        return db.inventoryDao()
    }
}