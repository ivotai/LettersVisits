package com.unicorn.lettersVisits.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.dylanc.viewbinding.inflate
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.googlematerial.RoundedGoogleMaterial
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.event.UserSelectEvent
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.data.model.role.RoleItemExpand
import com.unicorn.lettersVisits.databinding.ItemRoleBinding
import com.unicorn.lettersVisits.databinding.ItemUserBinding
import com.unicorn.lettersVisits.databinding.LayoutRoleUserListBinding


class UserSelectView(context: Context) : ConstraintLayout(context) {

    private val binding = inflate<LayoutRoleUserListBinding>()

    init {
        binding.apply {
            // crv, c for custom view
            crv.linear().setup {
                // for role itemExpand
                addType<RoleItemExpand>(R.layout.item_role)
                addType<User>(R.layout.item_user)

                onBind {
                    when (val item = getModel<Any>()) {
                        is RoleItemExpand -> {
                            val binding = getBinding<ItemRoleBinding>()
                            binding.tv.text = item.role.roleName

                            // 设置图标
                            val iconicsDrawable = binding.iv.drawable as IconicsDrawable
                            iconicsDrawable.icon =
                                if (item.itemExpand) RoundedGoogleMaterial.Icon.gmr_expand_more else RoundedGoogleMaterial.Icon.gmr_expand_less
                        }
                        is User -> {
                            val binding = getBinding<ItemUserBinding>()
                            binding.tv.text = item.username
                        }
                    }
                }
                R.id.root.onFastClick {
                    when (val item = getModel<Any>()) {
                        is RoleItemExpand -> {
                            expandOrCollapse()
                        }
                        is User -> {
                            sendEvent(UserSelectEvent(user = item))
                        }
                    }
                }

            }.models = Role.values().map { RoleItemExpand(it) }
        }
    }


}