package com.unicorn.lettersVisits.data.model.petition

import com.unicorn.lettersVisits.data.model.event.ExcelDialogEvent

class PetitionField(
    val label: String,
    val inputType: InputType = InputType.TEXT,
    var value: String = "",

    ) {

    lateinit var excelDialogEvent: ExcelDialogEvent

}