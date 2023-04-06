package com.unicorn.lettersVisits.ui.fra

import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.data.model.*
import com.unicorn.lettersVisits.data.model.event.PetitionerSelectEvent
import com.unicorn.lettersVisits.data.model.petition.Petition
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
                        tvUser.text = model.a1Name
//                        tvContent.text = model.content
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

    private fun getData(): MutableList<Petition> {
        ExcelData_.__ALL_PROPERTIES
        val petitionBox = Global.boxStore.boxFor<Petition>()
        val builder = petitionBox.query()
//            .orderDesc(Petition_.createTime)
//        builder.link(Petition_.department)
//            .apply(Department_.name
//            .equal(Global.currentUser!!.department.target.name))
        return builder.build().find()
    }


    override fun initIntents() {
        binding.apply {

        }
    }
}