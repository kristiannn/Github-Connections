package com.example.github_connections.repository.models

import android.os.Parcelable
import com.example.github_connections.database.entities.RepositoryEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepositoryWrapper(
    val name: String,
    val description: String,
    val watchersCount: Int,
    val forksCount: Int,
    val lastUpdated: String
) : Parcelable

fun RepositoryWrapper.mapToDb(username: String): RepositoryEntity {
    return RepositoryEntity(
        0,
        username,
        name,
        description,
        watchersCount,
        forksCount,
        lastUpdated)
}