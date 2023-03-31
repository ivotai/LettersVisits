package com.unicorn.lettersVisits.data.model.role

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Role(
    @Id var id: Long = 0, var name: String? = null
)

