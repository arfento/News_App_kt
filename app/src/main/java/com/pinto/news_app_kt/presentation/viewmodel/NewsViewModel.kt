package com.pinto.news_app_kt.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinto.news_app_kt.datasource.local.NewsRepository
import com.pinto.news_app_kt.presentation.model.NewsModel

class NewsViewModel : ViewModel() {

    private var newsLiveData: MutableLiveData<List<NewsModel>>? = null

    fun getNews(category: String?): MutableLiveData<List<NewsModel>>? {
        newsLiveData = category.let { NewsRepository().getNewsApiCall(it) }
        return newsLiveData
    }

    var newsData: LiveData<List<NewsModel>>? = null

    fun insertNews(context: Context, newsModel: NewsModel) {
        NewsRepository.insertNews(context, newsModel)
    }

    fun deleteNews(context: Context, newsModel: NewsModel) {
        NewsRepository.deleteNews(context, newsModel)
    }

    fun getNewsFromDB(context: Context): LiveData<List<NewsModel>>? {
        newsData = NewsRepository.getAllNews(context)
        return newsData
    }
}