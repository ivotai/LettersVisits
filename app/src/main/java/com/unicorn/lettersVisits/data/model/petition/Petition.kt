package com.unicorn.lettersVisits.data.model.petition

import com.unicorn.lettersVisits.data.model.Department
import com.unicorn.lettersVisits.data.model.User
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
    var name: String = "",
    var age: String = "",
    var gender: String = "",
    var category: String = "",
    var ethnicity: String = "",
    var occupation: String = "",

    ) : Serializable {
    //    lateinit var petitioner: ToOne<User>
    lateinit var creator: ToOne<User>
    lateinit var department: ToOne<Department>
}
