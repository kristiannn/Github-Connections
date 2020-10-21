package com.example.github_connections.modules.util

import android.app.Application
import android.content.Context

class UserAccountManager(
    application: Application
) : AccountManager {

    private val sharedPreferences = application.getSharedPreferences("account", Context.MODE_PRIVATE)
    private var accountName = "username"

    override fun getUser(): String {
        return sharedPreferences.getString(accountName, "")!!.toString()
    }

    override fun saveUser(username: String) {
        sharedPreferences.edit().putString(accountName, username).apply()
    }

    override fun isUserLogged(): Boolean {
        return sharedPreferences.contains(accountName)
    }

    override fun logoutUser() {
        sharedPreferences.edit().clear().apply()
    }
}