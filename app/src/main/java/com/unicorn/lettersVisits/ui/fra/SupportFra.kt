package com.unicorn.lettersVisits.ui.fra

import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.statusPadding
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.data.model.Support
import com.unicorn.lettersVisits.data.model.SupportHeader
import com.unicorn.lettersVisits.data.model.SupportType
import com.unicorn.lettersVisits.databinding.FraSupportBinding
import com.unicorn.lettersVisits.databinding.HeadSupportBinding
import com.unicorn.lettersVisits.databinding.ItemSupportBinding
import com.unicorn.lettersVisits.ui.base.BaseFra

class SupportFra : BaseFra<FraSupportBinding>() {

    override fun initViews() {
        binding.apply {

            rv.linear().divider {
                setDivider(width = 1, dp = true)
            }.setup {
                addType<Support>(R.layout.item_support)
                addType<SupportHeader>(R.layout.head_support)
                onBind {
                    when (val model = getModel<Any>()) {
                        is Support -> {
                            val binding = getBinding<ItemSupportBinding>()
                            binding.tv1.text = model.text
                        }
                        is SupportHeader -> {
                            val binding = getBinding<HeadSupportBinding>()
                            binding.tvRole.text = Global.role.text
                        }
                    }
                }
            }.models = listOf(
                Support(text = "案件流程判断辅助", supportType = SupportType.TOP),
                Support(text = "法律条款查询辅助", supportType = SupportType.MIDDLE),
                Support(text = "语音识别服务", supportType = SupportType.BOTTOM),
                Support(text = "常用计算工具辅助服务", supportType = SupportType.TOP),
                Support(text = "人工智能辅助", supportType = SupportType.BOTTOM),
            )

            rv.bindingAdapter.addHeader(SupportHeader())
        }
    }

    override fun initStatusBar() {
        binding.root.statusPadding()
    }

}