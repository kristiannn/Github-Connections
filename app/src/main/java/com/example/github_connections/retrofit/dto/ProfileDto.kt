package com.example.github_connections.retrofit.dto

import com.example.github_connections.repository.models.ProfileWrapper
import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("login")
    val name: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val bio: String?,
    val location: String?,
    @SerializedName("public_repos")
    val reposCount: Int,
    @SerializedName("followers")
    val followersCount: Int,
    @SerializedName("following")
    val followingCount: Int
)

fun ProfileDto.mapToRepository(lastUpdated: String?): ProfileWrapper {
    return ProfileWrapper(
        name,
        avatarUrl,
        bio ?: "This user has no bio",
        location ?: "Not specified",
        reposCount,
        followersCount,
        followingCount,
        lastUpdated ?: "0"
    )
}