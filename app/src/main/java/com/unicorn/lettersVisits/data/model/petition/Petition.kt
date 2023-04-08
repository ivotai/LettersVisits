package com.unicorn.lettersVisits.data.model.petition

import com.unicorn.lettersVisits.data.model.Department
import com.unicorn.lettersVisits.data.model.User
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import splitties.views.InputType.Companion.number
import splitties.views.InputType.Companion.phone
import java.io.Serializable
import java.util.*


@Entity
data class Petition(
    @Id var id: Long = 0,
//    var content: String = "",
    var createTime: Date = Date(),
    var reply: String = "",

    //
    var a1Name: String = "",
    var a2Age: String = "",
    var a3Gender: String = "",
    var a4Category: String = "",
    var a5Ethnicity: String = "",
    var a6Occupation: String = "",
    var a7DocumentType: String = "",
    var a8DocumentNumber: String = "",
    var a9PostalCode: String = "",
    var a10LandlinePhoneNumber: String = "",
    var a11MobilePhoneNumber: String = "",
    var a12NumberOfVisits: String = "",
    var a13LawyersLicenseNumber: String = "",
    var a14OrganizationCode: String = "",
    var a15CourtWithJurisdiction: String = "",
    var a16TypeOfCase:String = "",

    ) : Serializable {
    //    lateinit var petitioner: ToOne<User>
    lateinit var creator: ToOne<User>
    lateinit var department: ToOne<Department>

}
