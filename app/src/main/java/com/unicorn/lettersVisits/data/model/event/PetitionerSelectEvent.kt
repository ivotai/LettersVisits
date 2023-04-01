package com.unicorn.lettersVisits.data.model.event

import com.unicorn.lettersVisits.data.model.User

data class PetitionerSelectEvent(
    val petitioner: User
) {
}