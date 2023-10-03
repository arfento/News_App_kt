package com.pinto.news_app_kt.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.pinto.news_app_kt.MainActivity
import com.pinto.news_app_kt.R
import com.pinto.news_app_kt.databinding.FragmentGeneralBinding
import com.pinto.news_app_kt.presentation.activity.ReadNewsActivity
import com.pinto.news_app_kt.presentation.adapter.NewsAdapter
import com.pinto.news_app_kt.presentation.model.NewsModel
import com.pinto.news_app_kt.utils.Constants
import com.pinto.news_app_kt.utils.Constants.INITIAL_POSITION
import com.pinto.news_app_kt.utils.Constants.TOP_HEADLINES_COUNT
import com.squareup.picasso.Picasso

class GeneralFragment : Fragment() {
    private var _binding: FragmentGeneralBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var carouselView: CarouselView
    private lateinit var adapter: NewsAdapter
    private lateinit var newsDataForTopHeadlines: List<NewsModel>
    private lateinit var newsDataForDown: List<NewsModel>
    var position = INITIAL_POSITION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGeneralBinding.inflate(inflater, container, false)

        carouselView = binding.homeCarousel





        recyclerView = binding.recyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        newsDataForTopHeadlines = MainActivity.generalNews.slice(0 until TOP_HEADLINES_COUNT)
        newsDataForDown = MainActivity.generalNews.slice(TOP_HEADLINES_COUNT until MainActivity.generalNews.size - TOP_HEADLINES_COUNT)
         adapter = NewsAdapter(newsDataForDown)
        recyclerView.adapter = adapter

        carouselView.apply {
            size = newsDataForTopHeadlines.size
            autoPlay = true
            indicatorAnimationType = IndicatorAnimationType.THIN_WORM
            carouselOffset = OffsetType.CENTER
            setCarouselViewListener { view, position ->
                val imageView = view.findViewById<ImageView>(R.id.img)
                Picasso.get()
                    .load(newsDataForTopHeadlines[position].image)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.samplenews)
                    .into(imageView)


                val newsTitle = view.findViewById<TextView>(R.id.headline)
                newsTitle.text = newsDataForTopHeadlines[position].headLine

                view.setOnClickListener {

                    val intent = Intent(context, ReadNewsActivity::class.java).apply {
                        putExtra(Constants.NEWS_URL, newsDataForTopHeadlines[position].url)
                        putExtra(Constants.NEWS_TITLE, newsDataForTopHeadlines[position].headLine)
                        putExtra(Constants.NEWS_IMAGE_URL, newsDataForTopHeadlines[position].image)
                        putExtra(Constants.NEWS_DESCRIPTION, newsDataForTopHeadlines[position].description)
                        putExtra(Constants.NEWS_SOURCE, newsDataForTopHeadlines[position].source)
                        putExtra(Constants.NEWS_PUBLICATION_TIME, newsDataForTopHeadlines[position].time)
                        putExtra(Constants.NEWS_CONTENT, newsDataForTopHeadlines[position].content)
                    }

                    startActivity(intent)

                }
            }
            // After you finish setting up, show the CarouselView
            show()
        }
        adapter.setOnItemClickListener(object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, ReadNewsActivity::class.java).apply {
                    putExtra(Constants.NEWS_URL, newsDataForDown[position].url)
                    putExtra(Constants.NEWS_TITLE, newsDataForDown[position].headLine)
                    putExtra(Constants.NEWS_IMAGE_URL, newsDataForDown[position].image)
                    putExtra(Constants.NEWS_DESCRIPTION, newsDataForDown[position].description)
                    putExtra(Constants.NEWS_SOURCE, newsDataForDown[position].source)
                    putExtra(Constants.NEWS_PUBLICATION_TIME, newsDataForDown[position].time)
                    putExtra(Constants.NEWS_CONTENT, newsDataForDown[position].content)
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