package com.pinto.news_app_kt.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pinto.news_app_kt.R
import com.pinto.news_app_kt.databinding.ActivityMainBinding
import com.pinto.news_app_kt.databinding.ActivityReadNewsBinding
import com.pinto.news_app_kt.databinding.ActivitySaveNewsBinding
import com.pinto.news_app_kt.presentation.adapter.NewsAdapter
import com.pinto.news_app_kt.presentation.model.NewsModel
import com.pinto.news_app_kt.presentation.viewmodel.NewsViewModel
import com.pinto.news_app_kt.utils.Constants

class SaveNewsActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsData: MutableList<NewsModel>

    private var _binding: ActivitySaveNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySaveNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        newsData = mutableListOf()

        val adapter = NewsAdapter(newsData)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        // Get Saved News
        viewModel.getNewsFromDB(context = applicationContext)?.observe(this) {
            newsData.clear()
            newsData.addAll(it)
            adapter.notifyDataSetChanged()
        }
        adapter.setOnItemClickListener(object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@SaveNewsActivity, ReadNewsActivity::class.java).apply {
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
            override fun onItemLongClick(position: Int) {
                // Delete saved news dialog
                recyclerView.findViewHolderForAdapterPosition(position)?.itemView?.setBackgroundColor(
                    getThemeColor(com.google.android.material.R.attr.colorPrimaryVariant)
                )

                val alertDialog = AlertDialog.Builder(this@SaveNewsActivity).apply {
                    setMessage("Delete this News?")
                    setTitle("Alert!")
                    setCancelable(false)

                    setPositiveButton(
                        "Yes"
                    ) { _, _ ->
                        this@SaveNewsActivity.let {
                            viewModel.deleteNews(
                                it,
                                newsModel = newsData[position]
                            )
                        }
                        adapter.notifyItemRemoved(position)
                        Toast.makeText(this@SaveNewsActivity, "Deleted!", Toast.LENGTH_SHORT).show()
                    }

                    setNegativeButton("No") { _, _ ->
                        recyclerView.findViewHolderForAdapterPosition(position)?.itemView?.setBackgroundColor(
                            getThemeColor(com.google.android.material.R.attr.colorPrimary)
                        )
                    }
                }.create()

                alertDialog.show()
            }
        })

        recyclerView.adapter = adapter
    }

    @ColorInt
    fun Context.getThemeColor(@AttrRes attribute: Int) = TypedValue().let {
        theme.resolveAttribute(attribute, it, true)
        it.data
    }

}