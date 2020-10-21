package com.example.github_connections.repository

import com.example.github_connections.Result
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper

interface GithubRepository {

    suspend fun getRepos(username: String): Result<List<RepositoryWrapper>>

    suspend fun getFollowing(username: String): Result<List<FriendWrapper>>

    suspend fun getFollowers(username: String): Result<List<FriendWrapper>>

    suspend fun getProfile(username: String): Result<ProfileWrapper>
}