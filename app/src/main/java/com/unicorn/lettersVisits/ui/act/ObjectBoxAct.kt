package com.unicorn.lettersVisits.ui.act

import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.databinding.ActObjectBoxBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import io.objectbox.Box
import io.objectbox.BoxStore
import org.koin.android.ext.android.inject


// 这个界面用来执行一些数据库操作,测试
class ObjectBoxAct : BaseAct<ActObjectBoxBinding>() {

    private val boxStore: BoxStore by inject()

    private val roleBox: Box<Role> = boxStore.boxFor(Role::class.java)
    private val userBox: Box<User> = boxStore.boxFor(User::class.java)

    override fun initIntents() {
        userBox.all
        userBox.all

    }

}