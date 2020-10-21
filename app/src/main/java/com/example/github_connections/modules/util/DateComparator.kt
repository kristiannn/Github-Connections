package com.example.github_connections.modules.util

interface DateComparator {

    suspend fun isUpToDate(updatedTime: String?, databaseTime: String?): Boolean
}
