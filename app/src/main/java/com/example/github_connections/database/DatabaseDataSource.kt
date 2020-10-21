package com.example.github_connections.database

import com.example.github_connections.Result
import com.example.github_connections.database.entities.mapToRepository
import com.example.github_connections.repository.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseDataSource(private val githubDatabase: GithubDatabase) : DatabaseOperations {

    override suspend fun getRepos(username: String): Result<List<RepositoryWrapper>> =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAORepository().getRepos(username)?.let { repos ->
                val result = repos.map { it.mapToRepository() }
                Result.Success(result)
            } ?: Result.Error<List<RepositoryWrapper>>(DatabaseError.NullResult)
        }

    override suspend fun getFollowing(username: String): Result<List<FriendWrapper>> =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOFollowing().getFollowing(username)?.let { following ->
                val result = following.map { it.mapToRepository() }
                Result.Success(result)
            } ?: Result.Error<List<FriendWrapper>>(DatabaseError.NullResult)
        }

    override suspend fun getFollowers(username: String): Result<List<FriendWrapper>> =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOFollowers().getFollowers(username)?.let { followers ->
                val result = followers.map { it.mapToRepository() }
                Result.Success(result)
            } ?: Result.Error<List<FriendWrapper>>(DatabaseError.NullResult)
        }

    override suspend fun getProfile(username: String): Result<ProfileWrapper> =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOProfile().getProfile(username)?.let {
                Result.Success(it.mapToRepository())
            } ?: Result.Error<ProfileWrapper>(DatabaseError.NullResult)
        }

    override suspend fun saveFollowers(username: String, followersList: List<FriendWrapper>) =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOFollowers().deleteUserFollowers(username)
            for (element in followersList) {
                githubDatabase.dbDAOFollowers().insertFollower(element.mapToDbFollowers(username))
            }
        }

    override suspend fun saveFollowing(username: String, followingList: List<FriendWrapper>) =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOFollowing().deleteUserFollowings(username)
            for (element in followingList) {
                githubDatabase.dbDAOFollowing().insertFollowing(element.mapToDbFollowing(username))
            }
        }

    override suspend fun saveRepos(username: String, reposList: List<RepositoryWrapper>) =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAORepository().deleteUserRepos(username)
            for (element in reposList) {
                githubDatabase.dbDAORepository().insertRepo(element.mapToDb(username))
            }
        }

    override suspend fun saveProfile(username: String, profileWrapper: ProfileWrapper) =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOProfile().insertProfile(profileWrapper.mapToDb())
        }

    override suspend fun getProfileLastModified(username: String): String =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOProfile().getProfileLastModified(username)
        }

    override suspend fun getReposLastModified(username: String): String =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAORepository().getReposLastModified(username)
        }

    override suspend fun getFollowersLastModified(username: String): String =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOFollowers().getFollowersLastModified(username)
        }

    override suspend fun getFollowingLastModified(username: String): String =
        withContext(Dispatchers.IO) {
            githubDatabase.dbDAOFollowing().getFollowingLastModified(username)
        }
}