package com.unicorn.lettersVisits.ui.act

import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ColorUtils
import com.drake.statusbar.immersive
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.googlematerial.RoundedGoogleMaterial
import com.mikepenz.iconics.utils.colorInt
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.setUpWithViewPager2
import com.unicorn.lettersVisits.databinding.ActMain2Binding
import com.unicorn.lettersVisits.ui.base.BaseAct
import com.unicorn.lettersVisits.ui.fra.StaffPetitionFra
import com.unicorn.lettersVisits.ui.fra.SupportFra
import me.majiajie.pagerbottomtabstrip.item.NormalItemView


class StaffMainAct : BaseAct<ActMain2Binding>() {

    override fun initViews() {

        val tabCount = 2

        // init vp
        binding.vp.apply {
            isUserInputEnabled = false
            offscreenPageLimit = tabCount - 1
            adapter = object : FragmentStateAdapter(this@StaffMainAct) {

                override fun getItemCount() = tabCount

                override fun createFragment(position: Int) = when (position) {
                    0 -> StaffPetitionFra()
                    1 -> SupportFra()
                    else -> throw IllegalArgumentException()
                }
            }
        }

        fun initTab(viewPager2: ViewPager2) {

            fun newItem(vTitle: String, iconDefault: IIcon, iconChecked: IIcon) =
                NormalItemView(this).apply {
                    val defaultColor =
                        ColorUtils.getColor(splitties.material.colors.R.color.grey_400)
                    val checkedColor = ColorUtils.getColor(R.color.main_color)
                    title = vTitle
                    setDefaultDrawable(IconicsDrawable(context, iconDefault).apply {
                        colorInt = defaultColor
                    })
                    setTextDefaultColor(defaultColor)
                    setSelectedDrawable(IconicsDrawable(context, iconChecked).apply {
                        colorInt = checkedColor
                    })
                    setTextCheckedColor(checkedColor)
                }

            val navigationController = binding.tab.custom().addItem(
                newItem(
                    "信访申请",
                    RoundedGoogleMaterial.Icon.gmr_fact_check,
                    RoundedGoogleMaterial.Icon.gmr_fact_check,
                )
            ).addItem(
                newItem(
                    "辅助功能",
                    RoundedGoogleMaterial.Icon.gmr_support_agent,
                    RoundedGoogleMaterial.Icon.gmr_support_agent,
                )
            ).build()
            navigationController.setUpWithViewPager2(viewPager2 = viewPager2)
        }
        initTab(viewPager2 = binding.vp)
    }

    override fun initStatusBar() {
        immersive(darkMode = true)
    }

}