package com.unicorn.lettersVisits.data.model

enum class PetitionField(
    val label: String,
    val hint: String,
    val value:String
) {
    PF_PETITIONER("当事人", "请选择",""),
    PF_DEPARTMENT("信访单位", "请选择",""),
    PF_PETITION_TYPE("信访类型", "请选择",""),
    PF_REPLY("信访答复", "",""),
}