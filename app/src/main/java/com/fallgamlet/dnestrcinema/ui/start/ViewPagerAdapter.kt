package com.fallgamlet.dnestrcinema.ui.start

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val getCount: () -> Int,
    private val createPage: (position: Int) -> Fragment
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = getCount()

    override fun createFragment(position: Int): Fragment = createPage(position)

}
