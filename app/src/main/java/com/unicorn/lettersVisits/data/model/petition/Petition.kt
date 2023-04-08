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
//    var content: String = "",
    var createTime: Date = Date(),
    var reply: String = "",

    //
    var a1: String = "",
    var a2: String = "",
    var a3: String = "",
    var a4: String = "",
    var a5: String = "",
    var a6: String = "",
    var a7: String = "",
    var a8: String = "",
    var a9: String = "",
    var a10: String = "",
    var a11: String = "",
    var a12: String = "",
    var a13: String = "",
    var a14: String = "",
    var a15: String = "",
    var a16: String = "",

    ) : Serializable {
    //    lateinit var petitioner: ToOne<User>
    lateinit var creator: ToOne<User>
    lateinit var department: ToOne<Department>

}
