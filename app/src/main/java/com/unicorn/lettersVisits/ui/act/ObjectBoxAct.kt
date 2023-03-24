package com.unicorn.lettersVisits.ui.act

import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Role
import com.unicorn.lettersVisits.databinding.ActObjectBoxBinding
import com.unicorn.lettersVisits.ui.base.BaseAct


// 这个界面用来执行一些数据库操作
class ObjectBoxAct : BaseAct<ActObjectBoxBinding>() {

    override fun initIntents() {
        val box = SimpleComponent().boxStore.boxFor(Role::class.java)
        box.put(Role(name = "部委单位工作人员"))
        box.put(Role(name = "信访人"))
    }

}