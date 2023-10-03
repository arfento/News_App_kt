package com.pinto.news_app_kt.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pinto.news_app_kt.presentation.model.NewsModel
import com.pinto.news_app_kt.utils.Constants.DATABASE_NAME

@Database(entities = [NewsModel::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {

        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabaseClient(context: Context): NewsDatabase {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, NewsDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                return INSTANCE!!
            }

        }
    }
}