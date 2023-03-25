package com.unicorn.lettersVisits.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.dylanc.viewbinding.base.ActivityBinding
import com.dylanc.viewbinding.base.ActivityBindingDelegate


abstract class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity(),
    ActivityBinding<VB> by ActivityBindingDelegate() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithBinding()
    }

}


open class BaseAct<VB : ViewBinding> : BaseBindingActivity<VB>(), Ui {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    open fun init() {
        initStatusBar()
        initViews()
        initIntents()
        initEvents()
    }

    override fun initStatusBar() {
        immersive(darkMode = true)
        binding.root.statusPadding()
    }

    override fun initViews() {
    }

    override fun initIntents() {
    }

    override fun initEvents() {
    }

}