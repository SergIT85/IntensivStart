package ru.androidschool.intensiv.data.repository

import ru.androidschool.intensiv.data.dto.ActorCast

data class ActorResponse(
    var id: Int,
    var cast: List<ActorCast>
)
