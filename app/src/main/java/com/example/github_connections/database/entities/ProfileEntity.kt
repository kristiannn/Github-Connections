package com.example.github_connections.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.github_connections.repository.models.ProfileWrapper

@Entity
data class ProfileEntity(
    @PrimaryKey val name: String,
    val avatarUrl: String,
    val bio: String,
    val location: String,
    val reposCount: Int,
    val followersCount: Int,
    val followingCount: Int,
    @ColumnInfo(name = "lastUpdated")
    val lastUpdated: String
)

fun ProfileEntity.mapToRepository(): ProfileWrapper {
    return ProfileWrapper(
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