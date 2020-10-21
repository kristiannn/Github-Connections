package com.example.github_connections.modules

import com.example.github_connections.Result
import com.example.github_connections.database.DatabaseOperations
import com.example.github_connections.modules.util.InternetChecker
import com.example.github_connections.repository.GithubRepository
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper
import com.example.github_connections.retrofit.RestOperations
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Suppress("DeferredResultUnused")
class CachingDataSource(
    private val restDataSource: RestOperations,
    private val databaseDataSource: DatabaseOperations,
    private val internetCheckup: InternetChecker
) : GithubRepository {

    override suspend fun getFollowing(username: String): Result<List<FriendWrapper>> =
        if (internetCheckup.hasInternet()) {

            val upToDate =
                restDataSource.isDataUpToDate(username, databaseDataSource.getFollowingLastModified(username))

            if (upToDate) {
                databaseDataSource.getFollowing(username)
            } else {
                val result = restDataSource.getFollowing(username)

                if (result is Result.Success) {
                    coroutineScope{async { databaseDataSource.saveFollowing(username, result.data) }}
                }

                result
            }

        } else {
            databaseDataSource.getFollowing(username)
        }

    override suspend fun getFollowers(username: String): Result<List<FriendWrapper>> =
        if (internetCheckup.hasInternet()) {

            val upToDate =
                restDataSource.isDataUpToDate(username, databaseDataSource.getFollowersLastModified(username))

            if (upToDate) {
                databaseDataSource.getFollowers(username)
            } else {
                val result = restDataSource.getFollowers(username)

                if (result is Result.Success) {
                    coroutineScope{async {databaseDataSource.saveFollowers(username, result.data) }}
                }

                result
            }

        } else {
            databaseDataSource.getFollowers(username)
        }


    override suspend fun getRepos(username: String): Result<List<RepositoryWrapper>> =
        if (internetCheckup.hasInternet()) {

            val upToDate =
                restDataSource.isDataUpToDate(username, databaseDataSource.getReposLastModified(username))

            if (upToDate) {
                databaseDataSource.getRepos(username)

            } else {
                val result = restDataSource.getRepos(username)

                if (result is Result.Success) {
                    coroutineScope{async {databaseDataSource.saveRepos(username, result.data) }}
                }

                result
            }

        } else {
            databaseDataSource.getRepos(username)
        }


    override suspend fun getProfile(username: String): Result<ProfileWrapper> =
        if (internetCheckup.hasInternet()) {

            val upToDate =
                restDataSource.isDataUpToDate(username, databaseDataSource.getProfileLastModified(username))

            if (upToDate) {
                databaseDataSource.getProfile(username)

            } else {
                val result = restDataSource.getProfile(username)

                if (result is Result.Success) {
                    coroutineScope{async {databaseDataSource.saveProfile(username, result.data) }}
                }

                result
            }

        } else {
            databaseDataSource.getProfile(username)
        }

}