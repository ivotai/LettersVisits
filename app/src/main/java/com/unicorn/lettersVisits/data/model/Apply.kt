package com.unicorn.lettersVisits.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.Date


@Entity
data class Apply(
    @Id var id: Long = 0,
    var content: String? = null,
    var createTime:Date? = null,
) {
    lateinit var applicant: ToOne<User>
}