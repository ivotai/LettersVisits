package com.unicorn.lettersVisits.app

import android.content.Context
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.load.HttpException
import me.majiajie.pagerbottomtabstrip.NavigationController
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun NavigationController.setUpWithViewPager2(viewPager2: ViewPager2) {
    // todo 处理 remove
    addSimpleTabItemSelectedListener { index, _ -> viewPager2.setCurrentItem(index, false) }
//    val n: Int = viewPager2.currentItem
//    if (selected != n) {
//        setSelect(n)
//    }
    // todo 处理 remove
    viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (selected != position) {
                setSelect(position)
            }
        }
    })
}

fun TextView.trimText() = text.toString().trim()

fun String?.toast() = this.let { ToastUtils.showShort(it) }

fun ViewPager2.removeEdgeEffect() {
    (this.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
}





//fun Long.toDisplayDateFormat(): String = DateTime(this).toString(displayDateFormat)

@ColorInt
fun Context.getAttrColor(
    @AttrRes attrColor: Int, typedValue: TypedValue = TypedValue(), resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}
