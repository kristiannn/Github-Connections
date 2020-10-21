package com.example.github_connections.database

import com.example.github_connections.Result
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper

interface DatabaseOperations {

    suspend fun saveFollowers(username: String, followersList: List<FriendWrapper>)

    suspend fun saveFollowing(username: String, followingList: List<FriendWrapper>)

    suspend fun saveRepos(username: String, reposList: List<RepositoryWrapper>)

    suspend fun saveProfile(username: String, profileWrapper: ProfileWrapper)

    suspend fun getRepos(username: String): Result<List<RepositoryWrapper>>

    suspend fun getFollowing(username: String): Result<List<FriendWrapper>>

    suspend fun getFollowers(username: String): Result<List<FriendWrapper>>

    suspend fun getProfile(username: String): Result<ProfileWrapper>

    suspend fun getProfileLastModified(username: String): String

    suspend fun getReposLastModified(username: String): String

    suspend fun getFollowersLastModified(username: String): String

    suspend fun getFollowingLastModified(username: String): String
}