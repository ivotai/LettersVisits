package com.unicorn.lettersVisits.app

import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Department
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.role.Role
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import java.io.InputStream


// 这个界面用来执行一些数据库操作,测试
class ObjectBoxHelper {

    companion object {

        private val boxStore: BoxStore by lazy { SimpleComponent().boxStore }

        private val userBox: Box<User> = boxStore.boxFor()

        //        private val jurisdictionBox: Box<Jurisdiction> = boxStore.boxFor()
        private val departmentBox: Box<Department> = boxStore.boxFor()

        fun init() {
            if (departmentBox.isEmpty) {
                departmentBox.put(
                    Department(name = "部委单位1"),
                    Department(name = "部委单位2"),
                )
            }

            if (userBox.isEmpty) {
                userBox.put(
                    User(username = "张羡忠", role = Role.STAFF).apply {
                        department.target = departmentBox.all[0]
                    },
                    User(username = "杨间", role = Role.PETITIONER),
                    User(username = "童倩", role = Role.PETITIONER),
                )
            }
        }



    }

}