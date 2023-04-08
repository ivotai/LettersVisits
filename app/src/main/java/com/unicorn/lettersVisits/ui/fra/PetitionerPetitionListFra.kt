package com.unicorn.lettersVisits.ui.fra

import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.statusbar.statusPadding
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.data.model.petition.Petition
import com.unicorn.lettersVisits.data.model.event.PetitionerPutEvent
import com.unicorn.lettersVisits.databinding.FraApplyListBinding
import com.unicorn.lettersVisits.databinding.ItemApplyBinding
import com.unicorn.lettersVisits.ui.act.PetitionDetailAct
import com.unicorn.lettersVisits.ui.base.BaseFra
import io.objectbox.kotlin.boxFor
import io.objectbox.model.ModelProperty.addType
import splitties.fragments.start


class PetitionerPetitionListFra : BaseFra<FraApplyListBinding>() {

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
                        tvUser.text = model.a1
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

    override fun initIntents() {
        binding.apply {
            tvSearch.setOnClickListener {
                ToastUtils.showShort("搜索没做")
            }
            ivAdd.setOnClickListener {
                start<PetitionDetailAct> {

                }
            }
        }
    }

    override fun initEvents() {
        receiveEvent<PetitionerPutEvent> {
            binding.apply {
                rv.models = getData()
                rv.smoothScrollToPosition(0)
            }
        }
    }

    private fun getData(): MutableList<Petition> {
        val petitionBox = Global.boxStore.boxFor<Petition>()
        val builder = petitionBox.query()
//        builder.link(Petition_.petitioner)
//            .apply(User_.username.equal(Global.currentUser!!.username))
        return builder.build().find()
    }

    override fun initStatusBar() {
        binding.searchBarContainer.statusPadding()
    }

}