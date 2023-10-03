package com.pinto.news_app_kt.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pinto.news_app_kt.presentation.model.NewsModel
@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news : NewsModel)

    @Query("SELECT * FROM News_Table")
    fun getNewsFromDatabase(): LiveData<List<NewsModel>>

    @Delete
    fun deleteNews(news: NewsModel)

}