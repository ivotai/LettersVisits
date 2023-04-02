package com.unicorn.lettersVisits.ui.fra

import com.afollestad.materialdialogs.MaterialDialog
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
            Support.Support1,
            Support.Support2,
            Support.Support3,
            SupportDivider(),
            Support.Support4,
            Support.OpenAi,
            SupportDivider(),
            Support.CheckVersion,
            Support.Logout,
            SupportDivider()
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
                                tvUser.text = Global.currentUser!!.username
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
                            when (item) {
                                Support.Support1 -> {
                                    MaterialDialog(requireContext()).show {
                                        title(text = "这个看不懂")
                                        message(text = "1、提供案件流程判断辅助，登记的案件经过高院再审程序且为基层院一审的民事、行政案件则提示当事人，民事诉讼法\\行政司法解释，该类案件可向检查院提出抗诉")
                                    }
                                }
                                Support.Support2 -> {
                                    MaterialDialog(requireContext()).show {
                                        title(text = "网址是多少")
                                        message(text = "2、提供法律条款查询辅助，系统挂接法信系统，供用户查询法律条款。")
                                    }
                                }
                                Support.Support3 -> {
                                    MaterialDialog(requireContext()).show {
                                        title(text = "有必要做吗")
                                        message(text = "3、提供语音识别服务，可将用户谈话记录形成文档。")
                                    }
                                }
                                Support.Support4 -> {
                                    requireContext().start<CommonCalculationAct> {}
                                }
                                Support.OpenAi -> {
                                    requireContext().start<OpenAiAct> {}
                                }
                                Support.CheckVersion -> {
                                    ToastUtils.showShort("已是最新版本")
                                }
                                Support.Logout -> {
                                    Global.currentUser = null
                                    ActivityUtils.finishAllActivities()
                                    requireContext().start<LoginAct> {}
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