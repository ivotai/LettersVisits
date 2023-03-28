package com.unicorn.lettersVisits.app

import androidx.multidex.MultiDexApplication
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.module.objectBoxModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class SimpleApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SimpleApp.applicationContext)
            modules(objectBoxModule)
        }

        fun initStateConfig() {
            StateConfig.apply {
                emptyLayout = R.layout.layout_empty
                errorLayout = R.layout.layout_error
                loadingLayout = R.layout.layout_loading
            }
        }
        initStateConfig()

        fun initSmartRefreshLayout() {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { _, _ ->
                MaterialHeader(
                    this
                )
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { _, _ ->
                ClassicsFooter(
                    this
                )
            }
        }
        initSmartRefreshLayout()
    }

}


