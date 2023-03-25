package com.unicorn.lettersVisits.app

import com.unicorn.lettersVisits.data.model.Role
import com.unicorn.lettersVisits.data.model.User


object Global {

    var isLogin = false

    const val isObjectBoxMode = false

    lateinit var currentUser: User

    val currentRole: Role get() = currentUser.role.target

}