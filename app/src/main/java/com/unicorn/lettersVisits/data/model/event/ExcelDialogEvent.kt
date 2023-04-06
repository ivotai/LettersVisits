package com.unicorn.lettersVisits.data.model.event

import com.unicorn.lettersVisits.data.model.petition.PetitionField

data class ExcelDialogEvent
    (val queryValue: String, val queryIndex: Int, val petitionField: PetitionField) {}