package com.unicorn.lettersVisits.ui.act

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.drake.channel.receiveEvent
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.databinding.ActLoginBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import com.unicorn.lettersVisits.view.RoleUserListView
import splitties.activities.start
import splitties.resources.color

class LoginAct : BaseAct<ActLoginBinding>() {

    override fun initViews() {
        binding.apply {
            //
            btn1.helper.backgroundColorPressed = ColorUtils.blendARGB(
                color(R.color.main_color), Color.WHITE, 0.3f
            )
        }
    }


    private var userDialog: MaterialDialog? = null

    override fun initIntents() {
        binding.apply {
            btn1.setOnClickListener {
                fun showUserDialog() {
                    userDialog = MaterialDialog(this@LoginAct, BottomSheet()).show {
                        title(text = "请选择用户")
                        customView(view = RoleUserListView(context))
                    }
                }
                showUserDialog()
            }
        }
        // 初始化本地数据
        ObjectBoxHelper.init()
    }


    override fun initEvents() {
        receiveEvent<User> {
            userDialog?.dismiss()
            Global.currentUser = it
            if (Global.currentRole == Role.PETITIONER) start<PetitionerMainAct>() else start<Role1MainAct>()
            finish()
        }
    }


}