package com.unicorn.lettersVisits.data.model

import com.unicorn.lettersVisits.data.model.converter.PetitionTypeConverter
import com.unicorn.lettersVisits.data.model.role.PetitionType
import io.objectbox.annotation.Convert
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
    @Convert(
        converter = PetitionTypeConverter::class, dbType = String::class
    ) var petitionType: PetitionType = PetitionType.PetitionType1,
    var reply: String? = null,
) : Serializable {
    lateinit var petitioner: ToOne<User>
    lateinit var creator: ToOne<User>
    lateinit var department: ToOne<Department>
}
