package com.unicorn.lettersVisits.ui.act


import com.drake.channel.sendEvent
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Apply
import com.unicorn.lettersVisits.databinding.ActAddApplyBinding
import com.unicorn.lettersVisits.ui.base.BaseAct

class AddApplyAct : BaseAct<ActAddApplyBinding>() {

    override fun initIntents() {
        binding.apply {
            tvCancel.setOnClickListener {
                finish()
            }

            tvAddApply.setOnClickListener {
                val apply = Apply(content = etContent.text.toString()).apply {
                    applicant.target = Global.currentUser
                }
                SimpleComponent().boxStore.boxFor(Apply::class.java).put(apply)
                sendEvent(apply)
                finish()
            }
        }

    }

}