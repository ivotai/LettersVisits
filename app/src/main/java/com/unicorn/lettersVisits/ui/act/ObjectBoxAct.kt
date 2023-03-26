package com.unicorn.lettersVisits.ui.act

import com.unicorn.lettersVisits.data.model.Organization
import com.unicorn.lettersVisits.data.model.Region
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.User_
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.data.model.role.Role_
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
    private val regionBox: Box<Region> = boxStore.boxFor(Region::class.java)
    private val organizationBox: Box<Organization> = boxStore.boxFor(Organization::class.java)

    override fun initIntents() {
        val o = organizationBox.all.first()


        // get all Person objects named "Elmo"...
        val builder = userBox.query()
        builder.link(User_.role)
            .apply(Role_.name.notEqual("信访人"))
        val user = builder.build().findFirst()!!
        user
    }

}