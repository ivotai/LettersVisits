package com.unicorn.lettersVisits.ui.act

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.unicorn.lettersVisits.databinding.ActCommonCalculationBinding
import com.unicorn.lettersVisits.ui.base.BaseAct


class CommonCalculationAct : BaseAct<ActCommonCalculationBinding>() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView, request: WebResourceRequest
                ): Boolean {
                    view.loadUrl(request.url.toString())
                    return true
                }
            }
            settings.javaScriptEnabled = true
            loadUrl("http://weifayuan.court.gov.cn/gjzs2.0/pages/tool.html")
        }
    }

}