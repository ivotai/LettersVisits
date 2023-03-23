package com.unicorn.lettersVisits.ui.fra

import com.blankj.utilcode.util.SizeUtils.dp2px
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.statusPadding
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.data.model.support.Support
import com.unicorn.lettersVisits.data.model.support.SupportDivider
import com.unicorn.lettersVisits.data.model.support.SupportHeader
import com.unicorn.lettersVisits.data.model.support.SupportType
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
                addType<SupportDivider>(R.layout.divider_support)
                onBind {
                    when (val item = getModel<Any>()) {
                        is Support -> {
                            getBinding<ItemSupportBinding>().apply {
                                // 绘制背景
                                val helper = root.helper
                                val cornerRadius = dp2px(16f).toFloat()
                                when (item.supportType) {
                                    SupportType.TOP -> {
                                        helper.cornerRadiusTopLeft = cornerRadius
                                        helper.cornerRadiusTopRight = cornerRadius
                                    }
                                    SupportType.MIDDLE -> {
                                        // do nothing
                                    }
                                    SupportType.BOTTOM -> {
                                        helper.cornerRadiusBottomLeft = cornerRadius
                                        helper.cornerRadiusBottomRight = cornerRadius
                                    }
                                }

                                //
                                tv1.text = item.text
                            }
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
                SupportDivider(),
                Support(text = "常用计算工具辅助服务", supportType = SupportType.TOP),
                Support(text = "人工智能辅助", supportType = SupportType.BOTTOM),
                SupportDivider(),
                Support(text = "版本更新", supportType = SupportType.TOP),
                Support(text = "退出账户", supportType = SupportType.BOTTOM),
                SupportDivider(),
            )

            rv.bindingAdapter.addHeader(SupportHeader())
        }
    }

    override fun initStatusBar() {
        binding.root.statusPadding()
    }

}