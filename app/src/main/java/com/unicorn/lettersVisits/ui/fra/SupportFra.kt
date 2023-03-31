package com.unicorn.lettersVisits.ui.fra

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SizeUtils.dp2px
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.bindingAdapter
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
import com.unicorn.lettersVisits.ui.act.CommonCalculationAct
import com.unicorn.lettersVisits.ui.act.LoginAct
import com.unicorn.lettersVisits.ui.act.OpenAiAct
import com.unicorn.lettersVisits.ui.base.BaseFra
import splitties.activities.start
import splitties.resources.color

class SupportFra : BaseFra<FraSupportBinding>() {

    override fun initViews() {

        fun getData() = listOf(
            Support(
                text = "案件流程判断辅助",
                supportType = SupportType.TOP,
                splitties.material.colors.R.color.red_300
            ), Support(
                text = "法律条款查询辅助",
                supportType = SupportType.MIDDLE,
                splitties.material.colors.R.color.orange_300
            ), Support(
                text = "语音识别服务",
                supportType = SupportType.BOTTOM,
                splitties.material.colors.R.color.green_300
            ), SupportDivider(), Support(
                text = "常用计算工具辅助服务",
                supportType = SupportType.TOP,
                colorRes = splitties.material.colors.R.color.teal_300
            ), Support(
                text = "人工智能辅助",
                supportType = SupportType.BOTTOM,
                colorRes = splitties.material.colors.R.color.blue_300
            ), SupportDivider(), Support(
                text = "版本更新",
                supportType = SupportType.TOP,
                colorRes = splitties.material.colors.R.color.purple_300
            ), Support(
                text = "退出账户",
                supportType = SupportType.BOTTOM,
                colorRes = splitties.material.colors.R.color.indigo_300
            ), SupportDivider()
        )

        binding.apply {

            rv.linear().setup {

                addType<Support>(R.layout.item_support)
                addType<SupportHeader>(R.layout.head_support)
                addType<SupportDivider>(R.layout.divider_support)

                onBind {
                    when (val item = getModel<Any>()) {
                        is Support -> {
                            getBinding<ItemSupportBinding>().apply {
                                // 根据 supportType 绘制背景
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

                                // onBind
                                f.helper.backgroundColorNormal = color(item.colorRes)
                                tv.text = item.text
                            }
                        }
                        is SupportHeader -> {
                            getBinding<HeadSupportBinding>().apply {
                                tvUser.text = Global.currentUser!!.name
                                tvRole.text = Global.currentRole!!.roleName
                            }
                        }
                        is SupportDivider -> {
                            // do nothing
                        }
                    }
                }

                R.id.root.onFastClick {
                    when (val item = getModel<Any>()) {
                        is Support -> {
                            when (item.text) {
                                "案件流程判断辅助" -> {
                                    ToastUtils.showShort("看不懂这个需求")
                                }
                                "法律条款查询辅助" -> {
                                    ToastUtils.showShort("等待系统挂接法信系统，网址是多少？")
                                }
                                "语音识别服务" -> {
                                    ToastUtils.showShort("我觉得没必要做这个功能")
                                }
                                "常用计算工具辅助服务" -> {
                                    requireContext().start<CommonCalculationAct> {}
                                }
                                "人工智能辅助" -> {
                                    requireContext().start<OpenAiAct> {}
                                }
                                "版本更新" -> {
                                    ToastUtils.showShort("已是最新版本")
                                }
                                "退出账户" -> {
                                    Global.currentUser = null
                                    ActivityUtils.finishAllActivities()
                                    requireContext().start<LoginAct>()
                                }
                            }
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }

            }.models = getData()

            rv.bindingAdapter.addHeader(SupportHeader())
        }
    }

    override fun initStatusBar() {
        binding.root.statusPadding()
    }

}