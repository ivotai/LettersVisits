package com.unicorn.lettersVisits.data.model.role

import com.drake.brv.item.ItemExpand
import com.unicorn.lettersVisits.app.module.SimpleComponent
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Role(
    @Id var id: Long = 0, var name: String? = null
)

