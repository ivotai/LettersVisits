package com.unicorn.lettersVisits.ui.fra

import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.data.model.Apply
import com.unicorn.lettersVisits.databinding.FraApplyListBinding
import com.unicorn.lettersVisits.ui.base.BaseFra


class ApplyListFra: BaseFra<FraApplyListBinding>(){

    override fun initViews() {
        fun getApplyList(): List<Apply> {
            val content = "这段文字不用看这段文字不用看这段文字不用看这段文字不用看这段文字不用看这段文字不用看这段文字不用看这段文字不用看这段文字不用看这段文字不用看这段文字不用看这段文字不用看"
            val applyList = mutableListOf<Apply>()
            repeat(10) {
                applyList.add(Apply(content = content))
            }
            return applyList
        }

        binding.apply {
            rv.grid(1)
                .divider {
                    orientation = DividerOrientation.GRID
                    setDivider(width = 16, dp = true)
                includeVisible = true
            }.setup {
                addType<Apply>(R.layout.item_apply)
            }.models = getApplyList()
        }
    }

}