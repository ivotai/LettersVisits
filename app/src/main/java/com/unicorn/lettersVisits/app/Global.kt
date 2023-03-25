package com.unicorn.lettersVisits.app

import com.unicorn.lettersVisits.data.model.Roles
import com.unicorn.lettersVisits.data.model.User


object Global {

    var isLogin = false

    lateinit var roles: Roles

    val isObjectBoxMode = true

    lateinit var currentUser: User

    val currentRole get() = currentUser.role.target

}