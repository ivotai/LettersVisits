package com.unicorn.lettersVisits.app.module

import io.objectbox.BoxStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SimpleComponent : KoinComponent {

    val boxStore by inject<BoxStore>()

}