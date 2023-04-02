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
    var content: String? = null,
    var createTime: Date? = null,
    @Convert(
        converter = PetitionTypeConverter::class, dbType = String::class
    ) var petitionType: PetitionType? = null,
) : Serializable {
    lateinit var petitioner: ToOne<User>
    lateinit var creator: ToOne<User>
}
