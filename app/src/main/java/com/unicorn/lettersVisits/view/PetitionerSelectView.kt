package com.unicorn.lettersVisits.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.dylanc.viewbinding.inflate
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.User_
import com.unicorn.lettersVisits.data.model.event.StartOrcEvent
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.databinding.ItemUserBinding
import com.unicorn.lettersVisits.databinding.LayoutPetitionerSelectBinding
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder


class PetitionerSelectView(context: Context) : ConstraintLayout(context) {

    private val binding = inflate<LayoutPetitionerSelectBinding>()

    init {

        fun getData(): List<Any> {
            val userBox = SimpleComponent().boxStore.boxFor<User>()
            val query = userBox.query {
                equal(User_.role, Role.PETITIONER.roleName, QueryBuilder.StringOrder.CASE_SENSITIVE)
                orderDesc(User_.name)
            }
            return query.find()
        }

        binding.apply {
            // crv, c for custom view
            crv.linear().setup {

                addType<User>(R.layout.item_user)
                addType<String>(R.layout.item_user)

                onBind {
                    when (val item = getModel<Any>()) {
                        is User -> {
                            val binding = getBinding<ItemUserBinding>()
                            binding.tv.text = item.name
                        }
                        is String -> {
                            val binding = getBinding<ItemUserBinding>()
                            binding.tv.text = "扫描身份证录入新当事人"
                        }
                    }
                }
                R.id.root.onClick {
                    when (val item = getModel<Any>()) {
                        is User -> {
                            // 发送事件
                            sendEvent(event = item)
                        }
                        is String -> {
                            sendEvent(event = StartOrcEvent())
                        }
                    }
                }

            }.models = getData()

            binding.crv.bindingAdapter.addHeader("")
        }
    }


}