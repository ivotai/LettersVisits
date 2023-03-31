package com.unicorn.lettersVisits.data.model.converter

import com.unicorn.lettersVisits.data.model.role.Role
import io.objectbox.converter.PropertyConverter

class RoleConverter : PropertyConverter<Role?, String?> {
    override fun convertToEntityProperty(databaseValue: String?): Role? {
        if (databaseValue == null) {
            return null
        }
        for (role in Role.values()) {
            if (role.roleName == databaseValue) {
                return role
            }
        }
        return Role.STAFF
    }

    override fun convertToDatabaseValue(entityProperty: Role?): String? {
        return entityProperty?.roleName
    }
}