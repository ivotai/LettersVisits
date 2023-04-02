package com.unicorn.lettersVisits.ui.fra

import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Petition
import com.unicorn.lettersVisits.data.model.Petition_
import com.unicorn.lettersVisits.data.model.event.PetitionerSelectEvent
import com.unicorn.lettersVisits.databinding.FraRole2ApplyListBinding
import com.unicorn.lettersVisits.databinding.ItemApplyBinding
import com.unicorn.lettersVisits.ui.act.PetitionDetailAct
import com.unicorn.lettersVisits.ui.base.BaseFra
import io.objectbox.kotlin.boxFor
import splitties.fragments.start

class StaffPetitionListFra : BaseFra<FraRole2ApplyListBinding>() {

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
                        tvUser.text = model.petitioner.target.username
                        tvContent.text = model.content
                    }
                }
                onFastClick(R.id.root) {
                    val model = getModel<Petition>()
                    start<PetitionDetailAct> {
                        putExtra("id", model.id)
                    }
                }
            }.models = getData()
        }
    }

    override fun initEvents() {
        receiveEvent<PetitionerSelectEvent> {
            binding.rv.models = getData()
            binding.rv.smoothScrollToPosition(0)
        }
    }

    private fun getData(): MutableList<Petition> =
        SimpleComponent().boxStore.boxFor<Petition>().query().orderDesc(Petition_.createTime)
            .build().find()


    override fun initIntents() {
        binding.apply {

        }
    }
}