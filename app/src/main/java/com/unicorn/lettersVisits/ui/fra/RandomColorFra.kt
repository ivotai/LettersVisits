package com.unicorn.lettersVisits.ui.fra

import com.blankj.utilcode.util.ColorUtils
import com.unicorn.lettersVisits.databinding.FraRandomColorBinding
import com.unicorn.lettersVisits.ui.base.BaseFra


class RandomColorFra : BaseFra<FraRandomColorBinding>() {

    override fun initViews() {
        binding.root.setBackgroundColor(ColorUtils.getRandomColor(true))
    }

}