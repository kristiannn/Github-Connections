package com.example.github_connections.retrofit

import com.example.github_connections.Result
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper

interface RestOperations {

    suspend fun getRepos(username: String): Result<List<RepositoryWrapper>>

    suspend fun getFollowing(username: String): Result<List<FriendWrapper>>

    suspend fun getFollowers(username: String): Result<List<FriendWrapper>>

    suspend fun getProfile(username: String): Result<ProfileWrapper>

    suspend fun isDataUpToDate(username: String, dbTimeString: String): Boolean
}