package com.npwork.localise.model.auth

data class AuthVerify(
    val user: User,
    val project: Project,
    val group: Group
)
