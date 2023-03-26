package com.unicorn.lettersVisits.app

import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.data.model.User


object Global {

    const val isObjectBoxMode = true

    var currentUser: User? = null

    val currentRole: Role? get() = currentUser?.role?.target

    val isLogin get() = currentUser != null

}