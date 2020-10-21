package com.example.github_connections.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.github_connections.repository.models.FriendWrapper

@Entity(
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("user")
    )]
)
data class FollowersEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val user: String,
    val name: String,
    val avatarUrl: String,
    @ColumnInfo(name = "lastUpdated")
    val lastUpdated: String
)

fun FollowersEntity.mapToRepository(): FriendWrapper {
    return FriendWrapper(
        name,
        avatarUrl,
        lastUpdated
    )
}
