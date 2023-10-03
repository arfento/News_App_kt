package com.pinto.news_app_kt.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pinto.news_app_kt.MainActivity
import com.pinto.news_app_kt.R
import com.pinto.news_app_kt.databinding.FragmentBusinessBinding
import com.pinto.news_app_kt.presentation.activity.ReadNewsActivity
import com.pinto.news_app_kt.presentation.adapter.NewsAdapter
import com.pinto.news_app_kt.presentation.model.NewsModel
import com.pinto.news_app_kt.utils.Constants

class BusinessFragment : Fragment() {


    private var _binding: FragmentBusinessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBusinessBinding.inflate(inflater, container, false)
        val newsData: MutableList<NewsModel> = MainActivity.businessNews
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val adapter = NewsAdapter(newsData)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, ReadNewsActivity::class.java).apply {
                    putExtra(Constants.NEWS_URL, newsData[position].url)
                    putExtra(Constants.NEWS_TITLE, newsData[position].headLine)
                    putExtra(Constants.NEWS_IMAGE_URL, newsData[position].image)
                    putExtra(Constants.NEWS_DESCRIPTION, newsData[position].description)
                    putExtra(Constants.NEWS_SOURCE, newsData[position].source)
                    putExtra(Constants.NEWS_PUBLICATION_TIME, newsData[position].time)
                    putExtra(Constants.NEWS_CONTENT, newsData[position].content)
                }
                startActivity(intent)
            }
        })

        adapter.setOnItemLongClickListener(object : NewsAdapter.OnItemLongClickListener {
            override fun onItemLongClick(position: Int) = Unit
        })

        return binding.root
    }


}