package com.example.github_connections.database

sealed class DatabaseError(message: String) : Throwable(message) {

    object NullResult : DatabaseError ("Unable to find user at this moment!")
}