package com.unicorn.lettersVisits.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Role(
    @Id var id: Long = 0, var name: String? = null
)

/*
    box.put(Role(name = "部委单位工作人员"))
    box.put(Role(name = "信访人"))
 */