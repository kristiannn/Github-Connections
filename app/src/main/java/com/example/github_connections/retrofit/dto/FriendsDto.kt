package com.example.github_connections.retrofit.dto

import com.example.github_connections.repository.models.FriendWrapper
import com.google.gson.annotations.SerializedName

data class FriendsDto(
    @SerializedName("login")
    val name: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)

fun FriendsDto.mapToRepository(lastUpdated: String?): FriendWrapper {
    return FriendWrapper(
        name,
        avatarUrl,
        lastUpdated ?: "0"
    )
}