package com.example.github_connections.repository.models

import android.os.Parcelable
import com.example.github_connections.database.entities.ProfileEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileWrapper(
    val name: String,
    val avatarUrl: String,
    val bio: String,
    val location: String,
    val reposCount: Int,
    val followersCount: Int,
    val followingCount: Int,
    val lastUpdated: String
) : Parcelable

fun ProfileWrapper.mapToDb(): ProfileEntity {
    return ProfileEntity(
        name,
        avatarUrl,
        bio,
        location,
        reposCount,
        followersCount,
        followingCount,
        lastUpdated
    )
}