package com.unicorn.lettersVisits.data.model.converter

import com.unicorn.lettersVisits.data.model.role.PetitionType
import io.objectbox.converter.PropertyConverter

class PetitionTypeConverter : PropertyConverter<PetitionType?, String?> {
    override fun convertToEntityProperty(databaseValue: String?): PetitionType? {
        if (databaseValue == null) {
            return null
        }
        for (i in PetitionType.values()) {
            if (i.petitionTypeName == databaseValue) {
                return i
            }
        }
        return PetitionType.PetitionType1
    }

    override fun convertToDatabaseValue(entityProperty: PetitionType?): String? {
        return entityProperty?.petitionTypeName
    }
}