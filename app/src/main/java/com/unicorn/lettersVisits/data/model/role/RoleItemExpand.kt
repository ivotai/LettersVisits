package com.unicorn.lettersVisits.data.model.role

import com.drake.brv.item.ItemExpand
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Region
import com.unicorn.lettersVisits.data.model.Region_
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
            val box = SimpleComponent().boxStore.boxFor(Region::class.java)
//            box.query().equal(Region_.name, "东城区").build().find()

            // https://github.com/objectbox/objectbox-java/issues/1048 有 bug 暂时只能用4种类型查询
            val query = box.query().equal(Region_.name, "Joe",QueryBuilder.StringOrder.CASE_INSENSITIVE).build()
            val joes = query.find()
            query.close()



           return  listOf()
        }
        set(value) {}
    //        SimpleComponent().boxStore.boxFor<User>().query().equal(User_.role, Role.PETITIONER.id)
//            .build().find()

}