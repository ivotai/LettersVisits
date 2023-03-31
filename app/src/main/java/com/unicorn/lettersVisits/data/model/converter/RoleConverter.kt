package com.unicorn.lettersVisits.data.model.converter

import com.unicorn.lettersVisits.data.model.role.Role
import io.objectbox.converter.PropertyConverter

class RoleConverter : PropertyConverter<Role?, Int?> {
    override fun convertToEntityProperty(databaseValue: Int?): Role? {
        if (databaseValue == null) {
            return null
        }
        for (role in Role.values()) {
            if (role.id == databaseValue) {
                return role
            }
        }
        return Role.STAFF
    }

    override fun convertToDatabaseValue(entityProperty: Role?): Int? {
        return entityProperty?.id
    }
}