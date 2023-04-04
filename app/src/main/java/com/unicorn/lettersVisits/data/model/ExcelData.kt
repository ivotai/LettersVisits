package com.unicorn.lettersVisits.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
data class ExcelData(
    @Id var id: Long = 0,
    var projectName: String = "",
    var moduleName: String = "",
    var entryName: String = "",
    var level1: String = "",
    var level2: String = "",
    var level3: String = "",
    var level4: String = "",
    var level5: String = "",
    var level6: String = "",
)