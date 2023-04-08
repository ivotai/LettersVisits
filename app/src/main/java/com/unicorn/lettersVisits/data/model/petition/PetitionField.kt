package com.unicorn.lettersVisits.data.model.petition

import com.unicorn.lettersVisits.data.model.event.ExcelDialogEvent

class PetitionField(
    val label: String,
    val inputType: InputType = InputType.TEXT,
    var value: String = "",
    val allowEmpty: Boolean = true,
    val petitionFieldType: PetitionFieldType = PetitionFieldType.MIDDLE,
    var modelPosition: Int = 0,
    var petitionFieldPosition: Int = 0
) {

    lateinit var excelDialogEvent: ExcelDialogEvent

}