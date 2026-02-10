package dev.terry1921.nenektrivia.network.auth

data class AuthUser(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?
)
