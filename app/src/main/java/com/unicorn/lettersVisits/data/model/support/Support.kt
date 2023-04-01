package com.unicorn.lettersVisits.data.model.support

import com.unicorn.lettersVisits.R

enum class Support(
    val text: String, val supportType: SupportType, val colorRes: Int = R.color.main_color
) {
    Support1(
        text = "案件流程判断辅助",
        supportType = SupportType.TOP,
        colorRes = splitties.material.colors.R.color.red_300
    ),
    Support2(
        text = "法律条款查询辅助",
        supportType = SupportType.MIDDLE,
        colorRes = splitties.material.colors.R.color.orange_300
    ),
    Support3(
        text = "语音识别服务",
        supportType = SupportType.BOTTOM,
        colorRes = splitties.material.colors.R.color.green_300
    ),
    Support4(
        text = "常用计算工具辅助服务",
        supportType = SupportType.TOP,
        colorRes  = splitties.material.colors.R.color.teal_300
    ),
    OpenAi(
        text = "人工智能辅助",
        supportType = SupportType.BOTTOM,
        colorRes = splitties.material.colors.R.color.blue_300
    ),
    CheckVersion(
        text = "版本更新",
        supportType = SupportType.TOP,
        colorRes = splitties.material.colors.R.color.purple_300
    ),
    Logout(
        text = "退出账户",
        supportType = SupportType.BOTTOM,
        colorRes = splitties.material.colors.R.color.indigo_300
    ),

}
