package com.unicorn.lettersVisits.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ActivityUtils
import com.dylanc.viewbinding.inflate
import com.unicorn.lettersVisits.databinding.LayoutTitleBarBinding


// 自定义 View 的四个属性
/*
    1. 代码里创建，一个参数
    2. xml里创建，两个参数
    3. 后面两个参数是主题里的默认值，不管
 */
class TitleBar(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    private val binding = inflate<LayoutTitleBarBinding>()

    init {
        binding.apply {
            civ.setOnClickListener {
                ActivityUtils.getTopActivity().finish()
            }
        }
    }

    fun setTitle(title: String) {
        binding.apply {
            ctv.text = title
        }
    }

    fun getMoreView(): View {
        return binding.civ2
    }

    fun setOnMoreClickListener(listener: OnClickListener) {
        binding.civ2.visibility = View.VISIBLE
        binding.civ2.setOnClickListener(listener)
    }

}