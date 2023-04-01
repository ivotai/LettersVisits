package com.unicorn.lettersVisits.ui.fra

import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.statusbar.statusPadding
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Petition
import com.unicorn.lettersVisits.data.model.Petition_
import com.unicorn.lettersVisits.databinding.FraApplyListBinding
import com.unicorn.lettersVisits.databinding.ItemApplyBinding
import com.unicorn.lettersVisits.ui.act.AddPetitionAct
import com.unicorn.lettersVisits.ui.base.BaseFra
import io.objectbox.kotlin.boxFor
import splitties.fragments.start


class PetitionerApplyFra : BaseFra<FraApplyListBinding>() {

    override fun initViews() {
        binding.apply {
            rv.linear().divider {
                setDivider(width = 16, dp = true)
                endVisible = true
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
            }.models = getData()
        }
    }

    override fun initIntents() {
        binding.apply {
            tvSearch.setOnClickListener {
                ToastUtils.showShort("搜索没做")
            }
            ivAdd.setOnClickListener {
                start<AddPetitionAct> { }
            }
        }
    }

    override fun initEvents() {
        receiveEvent<Petition> {
            binding.apply {
                rv.models = getData()
                rv.smoothScrollToPosition(0)
            }
        }
    }

    private fun getData(): MutableList<Petition> =
        SimpleComponent().boxStore.boxFor<Petition>().query().orderDesc(Petition_.createTime)
            .build().find()

    override fun initStatusBar() {
        binding.searchBarContainer.statusPadding()
    }

}