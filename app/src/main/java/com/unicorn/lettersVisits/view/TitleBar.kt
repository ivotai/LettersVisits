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

/*
dxl|https://www.bilibili.com/video/BV1W8411s7Dj/
dxl|https://www.bilibili.com/video/BV1u84y1z7jm/
dxl|https://www.bilibili.com/video/BV1V84y1z7DS/
dxl|https://www.bilibili.com/video/BV1ue4y1S7wM/
dxl|https://www.bilibili.com/video/BV1nW4y1J7zP/
dxl|https://www.bilibili.com/video/BV1i8411e7We/
dxl|https://www.bilibili.com/video/BV138411e73Z/

dxl|https://www.bilibili.com/video/BV1gG4y1h775/
dxl|https://www.bilibili.com/video/BV1oG4y187YD/
dxl|https://www.bilibili.com/video/BV1pd4y1c7NV/
dxl|https://www.bilibili.com/video/BV1ue4y1t7yE/
dxl|https://www.bilibili.com/video/BV1Fd4y1w7iV/
dxl|https://www.bilibili.com/video/BV1ge4y1t7xU/
dxl|https://www.bilibili.com/video/BV1uP4y117wv/

dxl|https://www.bilibili.com/video/BV1sG4y147gw/

dxl|https://www.bilibili.com/video/BV1qG411M76N/
dxl|https://www.bilibili.com/video/BV11W4y1H7nn/
dxl|https://www.bilibili.com/video/BV1zD4y1e7SZ/
dxl|https://www.bilibili.com/video/BV1N8411j72a

dxl|https://www.bilibili.com/video/BV1524y1y7xm/
dxl|https://www.bilibili.com/video/BV1bg411W7jU/
dxl|https://www.bilibili.com/video/BV1t8411j7dR/
dxl|https://www.bilibili.com/video/BV1u44y1S7vf/
dxl|https://www.bilibili.com/video/BV17G4y1R7dQ/
dxl|https://www.bilibili.com/video/BV18M411z7nx/
dxl|https://www.bilibili.com/video/BV11D4y1h7Jt/

dxl|https://www.bilibili.com/video/BV1b14y1T7AL/
dxl|https://www.bilibili.com/video/BV18d4y1e7tw/
dxl|https://www.bilibili.com/video/BV1bM41117n1/
dxl|https://www.bilibili.com/video/BV1FA41197ZR/
dxl|https://www.bilibili.com/video/BV1a8411H7F2/
dxl|https://www.bilibili.com/video/BV1av4y1z7cD/
dxl|https://www.bilibili.com/video/BV1K3411X74c/


dxl|https://www.bilibili.com/video/BV1314y1w73d/
dxl|https://www.bilibili.com/video/BV1YP4y1v74R/
dxl|https://www.bilibili.com/video/BV1Fe4y1V7J5/
dxl|https://www.bilibili.com/video/BV1nY411m7Fx/
dxl|https://www.bilibili.com/video/BV11v4y1y7AZ/

 */