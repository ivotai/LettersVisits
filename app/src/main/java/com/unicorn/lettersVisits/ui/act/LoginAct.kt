package com.unicorn.lettersVisits.ui.act

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.drake.channel.receiveEvent
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.databinding.ActLoginBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import com.unicorn.lettersVisits.view.RoleUserListView
import splitties.activities.start
import splitties.resources.color

class LoginAct : BaseAct<ActLoginBinding>() {

    override fun initViews() {
        binding.apply {

            btn1.helper.backgroundColorPressed = ColorUtils.blendARGB(
                color(splitties.material.colors.R.color.blue_600), Color.WHITE, 0.3f
            )
        }
    }

    override fun initIntents() {
        binding.btn1.setOnClickListener {
            // 数据库模式
            if (Global.isObjectBoxMode) {
                start<ObjectBoxAct> {}
                return@setOnClickListener
            }

            fun showUserDialog() {
                userDialog = MaterialDialog(this, BottomSheet()).show {
                    title(text = "请选择用户")
                    customView(view = RoleUserListView(context))
                }
            }
            showUserDialog()
        }
    }

    private var userDialog: MaterialDialog? = null

    override fun initEvents() {
        receiveEvent<User> {
            userDialog?.dismiss()
            Global.currentUser = it
            // 信访人
            if (Global.currentRole!!.name != "信访人") start<Role1MainAct>() else start<Role2MainAct>()
            finish()
        }
    }


}