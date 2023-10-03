package com.pinto.news_app_kt.datasource.remote

import com.pinto.news_app_kt.presentation.model.NewsDataFromJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    fun getNews(@Query("country") country : String, @Query("category") category : String?, @Query("apiKey") key : String) : Call<NewsDataFromJson>

}