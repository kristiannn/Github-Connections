package com.example.github_connections.modules.util

class AccountManagerFake : AccountManager {

    override fun getUser(): String {
        return "Test Profile 1"
    }

    override fun saveUser(username: String) {
        return
    }

    override fun isUserLogged(): Boolean {
        return false
    }

    override fun logoutUser() {
        return
    }

}