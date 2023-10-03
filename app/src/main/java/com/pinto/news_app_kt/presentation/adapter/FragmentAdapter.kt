package com.pinto.news_app_kt.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pinto.news_app_kt.presentation.fragment.BusinessFragment
import com.pinto.news_app_kt.presentation.fragment.EntertainmentFragment
import com.pinto.news_app_kt.presentation.fragment.GeneralFragment
import com.pinto.news_app_kt.presentation.fragment.HealthFragment
import com.pinto.news_app_kt.presentation.fragment.ScienceFragment
import com.pinto.news_app_kt.presentation.fragment.SportFragment
import com.pinto.news_app_kt.presentation.fragment.TechFragment
import com.pinto.news_app_kt.utils.Constants.TOTAL_NEWS_TAB

class FragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle){

    override fun getItemCount(): Int = TOTAL_NEWS_TAB

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> {
                return GeneralFragment()
            }
            1 -> {
                return BusinessFragment()
            }
            2 -> {
                return EntertainmentFragment()
            }
            3 -> {
                return ScienceFragment()
            }
            4 -> {
                return SportFragment()
            }
            5 -> {
                return TechFragment()
            }
            6 -> {
                return HealthFragment()
            }

            else -> return BusinessFragment()

        }
    }
}