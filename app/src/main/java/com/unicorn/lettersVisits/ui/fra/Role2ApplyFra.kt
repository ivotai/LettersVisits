package com.unicorn.lettersVisits.ui.fra

import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.statusPadding
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Apply
import com.unicorn.lettersVisits.databinding.FraApplyListBinding
import com.unicorn.lettersVisits.databinding.ItemApplyBinding
import com.unicorn.lettersVisits.ui.base.BaseFra
import io.objectbox.kotlin.boxFor


class Role2ApplyFra : BaseFra<FraApplyListBinding>() {

    override fun initViews() {
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
            }.models = SimpleComponent().boxStore.boxFor<Apply>().all
        }
    }

    override fun initIntents() {
        binding.apply {
            tvSearch.setOnClickListener {
                ToastUtils.showShort("搜索没做")
            }
            ivAdd.setOnClickListener {
                ToastUtils.showShort("添加没做")
            }
        }
    }

    override fun initStatusBar() {
        binding.searchBarContainer.statusPadding()
    }

}