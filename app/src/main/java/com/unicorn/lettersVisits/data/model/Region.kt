package com.unicorn.lettersVisits.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
data class Region(
    @Id var id: Long = 0,
    var name: String? = null,
) {
}
