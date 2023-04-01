package com.unicorn.lettersVisits.app

import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Department
import com.unicorn.lettersVisits.data.model.Jurisdiction
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.role.Role
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor


// 这个界面用来执行一些数据库操作,测试
class ObjectBoxHelper {

    companion object {

        private val boxStore: BoxStore by lazy { SimpleComponent().boxStore }

        private val userBox: Box<User> = boxStore.boxFor()
        private val jurisdictionBox: Box<Jurisdiction> = boxStore.boxFor()
        private val departmentBox: Box<Department> = boxStore.boxFor()

        fun init() {
            if (userBox.isEmpty) {
                userBox.put(
                    User(name = "张羡忠", role = Role.STAFF),
                    User(name = "杨间", role = Role.PETITIONER),
                    User(name = "童倩", role = Role.PETITIONER),
                )
            }
        }

    }

}