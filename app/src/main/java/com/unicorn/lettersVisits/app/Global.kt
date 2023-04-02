package com.unicorn.lettersVisits.app

import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.role.Role


object Global {

    var currentUser: User? = null

    val currentRole: Role? get() = currentUser?.role

    val isStaff get() = currentRole == Role.STAFF

    val isPetitioner get() = currentRole == Role.PETITIONER

    val boxStore get() = SimpleComponent().boxStore

    val isLogin get() = currentUser != null

}