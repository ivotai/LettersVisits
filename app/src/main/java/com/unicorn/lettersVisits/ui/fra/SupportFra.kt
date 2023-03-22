package com.unicorn.lettersVisits.ui.fra

import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.data.model.Support
import com.unicorn.lettersVisits.databinding.FraSupportBinding
import com.unicorn.lettersVisits.databinding.ItemSupportBinding
import com.unicorn.lettersVisits.ui.base.BaseFra

class SupportFra : BaseFra<FraSupportBinding>() {

    override fun initViews() {
        binding.apply {

            rv.linear().setup {
                addType<Support>(R.layout.item_support)
                onBind {
                    val support = getModel<Support>()
                    val binding = getBinding<ItemSupportBinding>()
                    binding.tv.text = support.text
                }
            }.models = listOf(
                "案件流程判断辅助",
                "法律条款查询辅助",
                "语音识别服务",
                "常用计算工具辅助服务",
                "AI辅助",
            ).map {
                Support(it)
            }
        }
    }

}