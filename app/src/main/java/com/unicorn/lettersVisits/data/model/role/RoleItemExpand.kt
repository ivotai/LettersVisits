package com.unicorn.lettersVisits.data.model.role

import com.drake.brv.item.ItemExpand
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.User_
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder

class RoleItemExpand(val role: Role) : ItemExpand {

    /** 是否已展开 */
    override var itemExpand: Boolean = true

    /** 同级别的分组的索引位置 */
    override var itemGroupPosition = Role.values().indexOf(role)

    /** 子列表 */
    /** 子列表 */
    override var itemSublist: List<Any?>?
        get() {
            val userBox = SimpleComponent().boxStore.boxFor<User>()

            // https://github.com/objectbox/objectbox-java/issues/1048 有 bug 暂时只能用4种类型查询, string 要特别处理
            val query = userBox.query {
//                equal(User_.role, role.roleName, QueryBuilder.StringOrder.CASE_SENSITIVE)
                orderDesc(User_.name)
            }
            val list = query.find()

            return list
        }
        set(value) {}
    //        SimpleComponent().boxStore.boxFor<User>().query().equal(User_.role, Role.PETITIONER.id)
//            .build().find()

}