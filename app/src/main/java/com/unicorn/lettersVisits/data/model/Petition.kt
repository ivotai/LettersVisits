package com.unicorn.lettersVisits.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.io.Serializable
import java.util.*


@Entity
data class Petition(
    @Id var id: Long = 0,
    var content: String = "",
    var createTime: Date = Date(),
    var reply: String = "",

    //
    var name: String = ""

) : Serializable {
    //    lateinit var petitioner: ToOne<User>
    lateinit var creator: ToOne<User>
    lateinit var department: ToOne<Department>
}
