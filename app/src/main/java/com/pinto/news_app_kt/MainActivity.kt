package com.pinto.news_app_kt

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pinto.news_app_kt.databinding.ActivityMainBinding
import com.pinto.news_app_kt.presentation.activity.SaveNewsActivity
import com.pinto.news_app_kt.presentation.adapter.FragmentAdapter
import com.pinto.news_app_kt.presentation.model.NewsModel
import com.pinto.news_app_kt.presentation.viewmodel.NewsViewModel
import com.pinto.news_app_kt.utils.Constants.BUSINESS
import com.pinto.news_app_kt.utils.Constants.ENTERTAINMENT
import com.pinto.news_app_kt.utils.Constants.GENERAL
import com.pinto.news_app_kt.utils.Constants.HEALTH
import com.pinto.news_app_kt.utils.Constants.HOME
import com.pinto.news_app_kt.utils.Constants.SCIENCE
import com.pinto.news_app_kt.utils.Constants.SPORTS
import com.pinto.news_app_kt.utils.Constants.TECHNOLOGY
import com.pinto.news_app_kt.utils.Constants.TOTAL_NEWS_TAB

class MainActivity : AppCompatActivity() {

    private val newsCategories = arrayOf(
        HOME, BUSINESS,
        ENTERTAINMENT, SCIENCE,
        SPORTS, TECHNOLOGY, HEALTH
    )

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private var totalRequestCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        shimmerLayout = binding.shimmerLayout
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        if (!isNetworkAvailable(applicationContext)) {
            shimmerLayout.visibility = View.GONE
            val showError: TextView = binding.displayError
            showError.text = getString(R.string.internet_warming)
            showError.visibility = View.VISIBLE
        }

        requestNews(GENERAL, generalNews)
        requestNews(BUSINESS, generalNews)
        requestNews(ENTERTAINMENT, generalNews)
        requestNews(HEALTH, generalNews)
        requestNews(SCIENCE, generalNews)
        requestNews(SPORTS, generalNews)
        requestNews(TECHNOLOGY, generalNews)

        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = fragmentAdapter
        viewPager.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_mainactivity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        intent = Intent(applicationContext, SaveNewsActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    private fun requestNews(newsCategory: String, newsData: MutableList<NewsModel>) {
        viewModel.getNews(category = newsCategory)?.observe(this) {
            newsData.addAll(it)
            totalRequestCount += 1

            if (newsCategory == GENERAL) {
                shimmerLayout.stopShimmer()
                shimmerLayout.hideShimmer()
                shimmerLayout.visibility = View.GONE
                setViewPager()
            }
            if (totalRequestCount == TOTAL_NEWS_TAB) {
                binding.viewPager.offscreenPageLimit = 7
            }
        }
    }

    private fun setViewPager() {
        if (!apiRequestError) {
            viewPager.visibility = View.VISIBLE
            TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
                tab.text = newsCategories[position]
            }.attach()
        } else {
            val showError: TextView = binding.displayError
            showError.text = errorMessage
            showError.visibility = View.VISIBLE
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // For below 29 api
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }

    companion object {
        var generalNews: ArrayList<NewsModel> = ArrayList()
        var entertainmentNews: MutableList<NewsModel> = mutableListOf()
        var businessNews: MutableList<NewsModel> = mutableListOf()
        var healthNews: MutableList<NewsModel> = mutableListOf()
        var scienceNews: MutableList<NewsModel> = mutableListOf()
        var sportsNews: MutableList<NewsModel> = mutableListOf()
        var techNews: MutableList<NewsModel> = mutableListOf()
        var apiRequestError = false
        var errorMessage = "error"
    }

}