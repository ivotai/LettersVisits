package com.unicorn.lettersVisits.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.FragmentBinding
import com.dylanc.viewbinding.base.FragmentBindingDelegate


abstract class BaseBindingFragment<VB : ViewBinding> : Fragment(),
    FragmentBinding<VB> by FragmentBindingDelegate() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return createViewWithBinding(inflater, container)
    }
}


open class BaseFra<VB : ViewBinding> : BaseBindingFragment<VB>(), Ui {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    open fun init() {
        initViews()
        initIntents()
        initEvents()
        initStatusBar()
    }

    override fun initViews() {
    }

    override fun initIntents() {
    }

    override fun initEvents() {
    }

    override fun initStatusBar() {
    }

}