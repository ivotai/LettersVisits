package com.unicorn.lettersVisits.data.model

import com.unicorn.lettersVisits.data.model.role.Role
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne


@Entity
data class User(
    @Id var id: Long = 0,
    var name: String? = null,
) {
    lateinit var role: ToOne<Role>
}

/*
 */