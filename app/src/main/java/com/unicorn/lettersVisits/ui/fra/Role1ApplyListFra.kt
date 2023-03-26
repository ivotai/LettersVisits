package com.unicorn.lettersVisits.ui.fra

import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Apply
import com.unicorn.lettersVisits.databinding.FraRole2ApplyListBinding
import com.unicorn.lettersVisits.databinding.ItemApplyBinding
import com.unicorn.lettersVisits.ui.base.BaseFra
import io.objectbox.kotlin.boxFor

class Role1ApplyListFra : BaseFra<FraRole2ApplyListBinding>() {

    override fun initViews() {
        binding.apply {
            rv.linear().divider {
                setDivider(width = 16, dp = true)
                includeVisible = true
            }.setup {
                addType<Apply>(R.layout.item_apply)
                onBind {
                    val model = getModel<Apply>()
                    val binding = getBinding<ItemApplyBinding>()
                    binding.apply {
                        tvUser.text = model.applicant.target.name
                        tvContent.text = model.content
                    }
                }
            }.models = SimpleComponent().boxStore.boxFor<Apply>().all
        }
    }

}