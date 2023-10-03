package com.pinto.news_app_kt.presentation.model

import com.example.newsapp.retrofit.Article

data class NewsDataFromJson(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)