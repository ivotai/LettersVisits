package com.unicorn.lettersVisits.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.dylanc.viewbinding.inflate
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Role
import com.unicorn.lettersVisits.data.model.RoleWrapper
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.databinding.ItemRoleBinding
import com.unicorn.lettersVisits.databinding.ItemUserBinding
import com.unicorn.lettersVisits.databinding.LayoutUserListBinding


class UserListView(context: Context) : ConstraintLayout(context) {

    private val binding = inflate<LayoutUserListBinding>()

    init {
        binding.apply {
            val getData = {
                val roleList = SimpleComponent().boxStore.boxFor(Role::class.java).all
                roleList.map { RoleWrapper(it) }
            }


            crv.linear().setup {
                // 任何条目都需要添加类型到BindingAdapter中
                addType<RoleWrapper>(R.layout.item_role)
                addType<User>(R.layout.item_user)
                onBind {
                    val item = getModel<Any>()
                    when (item) {
                        is RoleWrapper -> {
                            val binding = getBinding<ItemRoleBinding>()
                            val role = item.role
                            binding.tv.text = role.name
                        }
                        is User -> {
                            val binding = getBinding<ItemUserBinding>()
                            val user = item
                            binding.tv.text = user.name
                        }
                    }
                }
                R.id.root.onClick {
                    expandOrCollapse() // 展开或者折叠
                }

            }.models = getData()
        }
    }


}