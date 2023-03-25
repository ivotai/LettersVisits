package com.unicorn.lettersVisits.app.module

import com.unicorn.lettersVisits.data.model.MyObjectBox
import com.unicorn.lettersVisits.data.model.Role
import com.unicorn.lettersVisits.data.model.User
import io.objectbox.BoxStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val objectBoxModule = module {

    single {
        // koin 需要用 androidContext() 来提供 context
        val boxStore = MyObjectBox.builder().androidContext(androidContext()).build();
        boxStore
    }

    factory {
        get<BoxStore>().boxFor(Role::class.java)
    }

    factory {
        get<BoxStore>().boxFor(User::class.java)
    }

}