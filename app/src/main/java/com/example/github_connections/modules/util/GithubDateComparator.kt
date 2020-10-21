package com.example.github_connections.modules.util

import java.text.SimpleDateFormat
import java.util.*

class GithubDateComparator : DateComparator {

    private val dateFormatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault())

    override suspend fun isUpToDate(updatedTime: String?, databaseTime: String?): Boolean {
        if (updatedTime.isNullOrEmpty() || databaseTime.isNullOrEmpty() || databaseTime == "0") {
            return false
        }

        val upTime: Date = dateFormatter.parse(updatedTime)!!
        val dbTime: Date = dateFormatter.parse(databaseTime)!!
        val formattedUpTime = dateFormatter.format(upTime)
        val formattedDbTime = dateFormatter.format(dbTime)

        return formattedUpTime <= formattedDbTime
    }
}
