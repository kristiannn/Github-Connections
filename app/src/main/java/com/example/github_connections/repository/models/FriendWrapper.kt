package com.example.github_connections.repository.models

import android.os.Parcelable
import com.example.github_connections.database.entities.FollowersEntity
import com.example.github_connections.database.entities.FollowingEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FriendWrapper(
    val name: String,
    val avatarUrl: String,
    val lastUpdated: String
) : Parcelable

fun FriendWrapper.mapToDbFollowers(username: String): FollowersEntity {
    return FollowersEntity(0, username, name, avatarUrl, lastUpdated)
}

fun FriendWrapper.mapToDbFollowing(username: String): FollowingEntity {
    return FollowingEntity(0, username, name, avatarUrl, lastUpdated)
}