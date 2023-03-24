package com.unicorn.lettersVisits.ui.act

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.data.model.Roles
import com.unicorn.lettersVisits.databinding.ActLoginBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
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
            fun showRoleDialog() {
                MaterialDialog(this, BottomSheet(LayoutMode.MATCH_PARENT)).show {
                    lifecycleOwner(this@LoginAct)
                    title(text = "请选择您的身份")
                    listItems(items = Roles.values().map { it.text }) { _, index, _ ->
                        Global.roles = Roles.values()[index]
                        if (Global.roles == Roles.Roles1) start<ObjectBoxAct>() else start<ObjectBoxAct>()
                    }
                }
            }
            showRoleDialog()
        }
    }

}