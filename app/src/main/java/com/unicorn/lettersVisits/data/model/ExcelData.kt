package com.unicorn.lettersVisits.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
data class ExcelData(
    var d1: String = "",
    var d2: String = "",
    var d3: String = "",
    var d4: String = "",
    var d5: String = "",
    var d6: String = "",
    var d7: String = "",
    var d8: String = "",
    var d9: String = "",
    @Id var id: Long = 0,
)