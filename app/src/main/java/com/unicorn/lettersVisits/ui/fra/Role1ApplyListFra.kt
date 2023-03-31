package com.unicorn.lettersVisits.ui.fra

import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Petition
import com.unicorn.lettersVisits.data.model.Petition_
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
                addType<Petition>(R.layout.item_apply)
                onBind {
                    val model = getModel<Petition>()
                    val binding = getBinding<ItemApplyBinding>()
                    binding.apply {
                        tvUser.text = model.petitioner.target.name
                        tvContent.text = model.content
                    }
                }
            }.models =
                SimpleComponent().boxStore.boxFor<Petition>().query().order(Petition_.createTime).build()
                    .find()
        }
    }

}