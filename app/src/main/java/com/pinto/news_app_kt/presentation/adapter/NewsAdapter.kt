package com.pinto.news_app_kt.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pinto.news_app_kt.R
import com.pinto.news_app_kt.databinding.ListItemBinding
import com.pinto.news_app_kt.presentation.model.NewsModel
import com.squareup.picasso.Picasso
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class NewsAdapter(private var newsList: List<NewsModel>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mClickListener: OnItemClickListener
    private lateinit var mLongClickListener: OnItemLongClickListener

    init {
        this.notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mLongClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding, mClickListener, mLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val newsData = newsList[holder.adapterPosition]
        holder.headLine

        holder.headLine.text = newsData.headLine
        val time: String? = newsData.time
        val imgUrl = newsData.image

        if (imgUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(R.drawable.samplenews)
                .fit()
                .centerCrop()
                .into(holder.image)
        } else {
            Picasso.get()
                .load(imgUrl)
                .fit()
                .centerCrop()
                .error(R.drawable.samplenews)
                .into(holder.image)
        }

        if (context.toString().contains("SavedNews")) {
            val date = " " + time?.substring(0, time.indexOf('T', 0))
            holder.newsPublicationTime.text = date
        } else {
            val currentTimeInHours = Instant.now().atZone(ZoneId.of("Asia/Tokyo"))
            val newsTimeInHours = Instant.parse(time).atZone(ZoneId.of("Asia/Tokyo"))
            val hoursDifference = Duration.between(currentTimeInHours, newsTimeInHours)
            val hoursAgo = " " + hoursDifference.toHours().toString().substring(1) + " hour ago"
            holder.newsPublicationTime.text = hoursAgo
        }

    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(
        binding : ListItemBinding,
        listener: OnItemClickListener,
        listener2: OnItemLongClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.img
        val headLine: TextView = binding.newsTitle
        val newsPublicationTime: TextView = binding.newsPublicationTime

        init {
            binding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

            binding.root.setOnLongClickListener {
                listener2.onItemLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

    }

}
