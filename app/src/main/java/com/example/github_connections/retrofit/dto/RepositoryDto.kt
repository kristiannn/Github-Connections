package com.example.github_connections.retrofit.dto

import com.example.github_connections.repository.models.RepositoryWrapper
import com.google.gson.annotations.SerializedName

data class RepositoryDto(
    val name: String,
    val description: String?,
    @SerializedName("watchers_count")
    val watchersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int
)

fun RepositoryDto.mapToRepository(lastUpdated: String?): RepositoryWrapper {
    return RepositoryWrapper(
        name,
        description ?: "No description.",
        watchersCount,
        forksCount,
        lastUpdated ?: "0"
    )
}