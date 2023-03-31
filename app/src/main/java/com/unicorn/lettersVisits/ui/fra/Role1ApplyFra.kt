package com.unicorn.lettersVisits.ui.fra

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.drake.statusbar.statusPadding
import com.google.android.material.tabs.TabLayoutMediator
import com.unicorn.lettersVisits.databinding.FraRole2ApplyBinding
import com.unicorn.lettersVisits.ui.base.BaseFra

class Role1ApplyFra : BaseFra<FraRole2ApplyBinding>() {

    override fun initViews() {

        val tabCount = 3

        // init vp
        binding.vp.apply {
            offscreenPageLimit = tabCount - 1
            adapter = object : FragmentStateAdapter(this@Role1ApplyFra) {

                override fun getItemCount() = tabCount

                override fun createFragment(position: Int) = when (position) {
                    0 -> Role1ApplyListFra()
                    1 -> RandomColorFra()
                    2 -> RandomColorFra()
                    else -> throw IllegalArgumentException()
                }
            }
        }

        binding.apply {
            TabLayoutMediator(tab, vp) { tab, position ->
                tab.text = when (position) {
                    0 -> "部委"
                    1 -> "辖区"
                    2 -> "关注"
                    else -> throw IllegalArgumentException()
                }
            }.attach()
        }

    }

    override fun initStatusBar() {
        binding.searchBarContainer.statusPadding()
    }

}