package com.unicorn.lettersVisits.data.model

import com.unicorn.lettersVisits.data.model.converter.RoleConverter
import com.unicorn.lettersVisits.data.model.role.Role
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.io.Serializable


@Entity
data class User(
    @Id var id: Long = 0,
    var username: String? = null,
    var password: String? = null,
    @Convert(converter = RoleConverter::class, dbType = String::class) var role: Role? = null,
    var address: String? = null,
    var birthday: String? = null,
    var ethnic: String? = null,
    var gender: String? = null,
    var idNumber: String? = null,
    var name: String? = null,
) : Serializable {

    lateinit var department: ToOne<Department>

}

/*
 */