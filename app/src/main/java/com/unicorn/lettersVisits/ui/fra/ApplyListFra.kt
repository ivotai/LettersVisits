package com.unicorn.lettersVisits.ui.fra

import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.statusPadding
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.data.model.Apply
import com.unicorn.lettersVisits.databinding.FraApplyListBinding
import com.unicorn.lettersVisits.databinding.ItemApplyBinding
import com.unicorn.lettersVisits.ui.base.BaseFra


class ApplyListFra : BaseFra<FraApplyListBinding>() {

    override fun initViews() {
        fun getApplyList(): List<Apply> {
            val content = "这是申请内容这是申请内容这是申请内容这是申请内容这是申请内容这是申请内容这是申请内容这是申请内容这是申请内容这是申请内容这是申请内容这是申请内容"
            val applyList = mutableListOf<Apply>()
            repeat(10) {
                applyList.add(Apply(content = content))
            }
            return applyList
        }

        binding.apply {
            rv.linear().divider {
                setDivider(width = 16, dp = true)
                endVisible = true
            }.setup {
                addType<Apply>(R.layout.item_apply)
                onBind {
                    val model = getModel<Apply>()
                    val binding = getBinding<ItemApplyBinding>()
                    binding.apply {
                        tvContent.text = model.content
                    }
                }
            }.models = getApplyList()
        }
    }

    override fun initIntents() {
        binding.apply {
            tvSearch.setOnClickListener {
                ToastUtils.showShort("搜索还没做")
            }
            ivAdd.setOnClickListener {
                ToastUtils.showShort("添加还没做")
            }
        }
    }

    override fun initStatusBar() {
        binding.searchBarContainer.statusPadding()
    }

}