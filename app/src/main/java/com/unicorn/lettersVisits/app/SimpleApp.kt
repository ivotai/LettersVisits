package com.unicorn.lettersVisits.app

import androidx.multidex.MultiDexApplication
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.unicorn.lettersVisits.R


class SimpleApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        fun initStateConfig() {
            StateConfig.apply {
                emptyLayout = R.layout.layout_empty
                errorLayout = R.layout.layout_error
                loadingLayout = R.layout.layout_loading
            }
        }
        initStateConfig()

        fun initSmartRefreshLayout() {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                MaterialHeader(
                    this
                )
            }

            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                ClassicsFooter(
                    this
                )
            }
        }
        initSmartRefreshLayout()
    }

}


