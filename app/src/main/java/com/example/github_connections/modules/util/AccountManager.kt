package com.example.github_connections.modules.util

interface AccountManager {

    fun getUser(): String

    fun saveUser(username: String)

    fun isUserLogged(): Boolean

    fun logoutUser()
}