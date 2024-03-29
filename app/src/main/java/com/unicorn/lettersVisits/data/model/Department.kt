package com.unicorn.lettersVisits.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne


@Entity
data class Department(
    @Id var id: Long = 0,
    var name: String = "",
    var departmentType: Int = 0,   // 1法院,2政法委,3信访局
) {
    lateinit var jurisdiction: ToOne<Jurisdiction>
}
