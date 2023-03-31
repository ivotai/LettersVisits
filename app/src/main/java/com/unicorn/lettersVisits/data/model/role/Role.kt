package com.unicorn.lettersVisits.data.model.role

enum class Role(val id: Int, val mName: String) {
    STAFF(id = 0, mName = "部位单位工作人员"),
    PETITIONER(id = 1, mName = "当事人"),
}