package com.example.connectapisupabase.domain.model

import java.io.File

data class UserRequest(
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val verified: Boolean? = null,
    val name:String? = null,
    val avatar: File? = null,
    val emailVisibility: Boolean? = null
)