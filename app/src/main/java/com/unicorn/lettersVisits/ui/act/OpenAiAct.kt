package com.unicorn.lettersVisits.ui.act

import android.graphics.Color
import android.util.Log
import androidx.core.graphics.ColorUtils
import com.drake.softinput.hasSoftInput
import com.drake.softinput.setWindowSoftInput
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.databinding.ActOpenAiBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import splitties.resources.color


class OpenAiAct: BaseAct<ActOpenAiBinding>() {


    override fun initViews() {
        binding.apply {
            //
            btnSend.helper.backgroundColorPressed = ColorUtils.blendARGB(
                color(R.color.main_color), Color.BLACK, 0.3f
            )
        }
        setWindowSoftInput(
            float = binding.RConstraintLayout,
            onChanged = {
                Log.d("SoftInput", "visibility = ${hasSoftInput()}")
            }
        )
    }
}