package com.unicorn.lettersVisits.data.model.role

import com.drake.brv.item.ItemExpand
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.User_

class RoleItemExpand(val role: Role) : ItemExpand {

    /** 是否已展开 */
    override var itemExpand: Boolean = true

    /** 同级别的分组的索引位置 */
    override var itemGroupPosition = role.id.toInt()

    /** 子列表 */
    override var itemSublist: List<Any?>? =
        SimpleComponent().boxStore.boxFor(User::class.java).query().equal(User_.roleId, role.id)
            .build().find()

}