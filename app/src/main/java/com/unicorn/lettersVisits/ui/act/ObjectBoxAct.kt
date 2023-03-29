package com.unicorn.lettersVisits.ui.act

import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Organization
import com.unicorn.lettersVisits.data.model.Region
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.role.Role
import io.objectbox.Box
import io.objectbox.BoxStore


// 这个界面用来执行一些数据库操作,测试
class ObjectBoxAct() {

    companion object {

        private val boxStore: BoxStore by lazy { SimpleComponent().boxStore }

        private val roleBox: Box<Role> = boxStore.boxFor(Role::class.java)
        private val userBox: Box<User> = boxStore.boxFor(User::class.java)
        private val regionBox: Box<Region> = boxStore.boxFor(Region::class.java)
        private val organizationBox: Box<Organization> = boxStore.boxFor(Organization::class.java)
        fun init() {
            if (roleBox.all.size != 0) return

            val role1 = Role(name = "部委单位工作人员")
            val role2 = Role(name = "当事人")
            roleBox.put(role1, role2)

            val user1 = User(name = "张羡忠").apply {
                role.target = role1
            }
            val user2 = User(name = "杨间").apply {
                role.target = role2
            }
            val user3 = User(name = "李四").apply {
                role.target = role2
            }
            userBox.put(user1, user2, user3)
        }

    }

}