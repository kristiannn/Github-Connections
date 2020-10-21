package com.example.github_connections.retrofit

import com.example.github_connections.Result
import com.example.github_connections.modules.util.DateComparator
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper
import com.example.github_connections.retrofit.dto.mapToRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class RestDataSource(
    private val gitService: GitService,
    private val dateComparator: DateComparator
) : RestOperations {

    override suspend fun getRepos(username: String): Result<List<RepositoryWrapper>> =
        withContext(Dispatchers.IO) {
            try {
                val response = gitService.getRepos(username).execute()

                if (!response.isSuccessful)
                    return@withContext Result.Error<List<RepositoryWrapper>>(
                        RestError.HttpError(
                            response.message(),
                            response.code()
                        )
                    )

                try {
                    val lastUpdated = gitService.getProfileHeader(username).execute()

                    if (!lastUpdated.isSuccessful) {
                        return@withContext Result.Error<List<RepositoryWrapper>>(
                            RestError.HttpError(
                                lastUpdated.message(),
                                lastUpdated.code()
                            )
                        )
                    }

                    val result = response.body()
                        ?: return@withContext Result.Error<List<RepositoryWrapper>>(RestError.EmptyBody)

                    return@withContext Result.Success(result.map { it.mapToRepository(lastUpdated.headers().get("Last-Modified").toString()) })

                } catch (e: IOException) {
                    val result = response.body()
                        ?: return@withContext Result.Error<List<RepositoryWrapper>>(RestError.EmptyBody)

                    return@withContext Result.Success(result.map { it.mapToRepository("0") })
                }

            } catch (e: IOException) {
                return@withContext Result.Error<List<RepositoryWrapper>>(RestError.NetworkError)
            }
        }

    override suspend fun getFollowing(username: String): Result<List<FriendWrapper>> =
        withContext(Dispatchers.IO) {
            try {
                val response = gitService.getFollowing(username).execute()

                if (!response.isSuccessful)
                    return@withContext Result.Error<List<FriendWrapper>>(
                        RestError.HttpError(
                            response.message(),
                            response.code()
                        )
                    )

                try {
                    val lastUpdated = gitService.getProfileHeader(username).execute()

                    if (!lastUpdated.isSuccessful) {
                        return@withContext Result.Error<List<FriendWrapper>>(
                            RestError.HttpError(
                                lastUpdated.message(),
                                lastUpdated.code()
                            )
                        )
                    }

                    val result =
                        response.body() ?: return@withContext Result.Error<List<FriendWrapper>>(RestError.EmptyBody)

                    return@withContext Result.Success(result.map { it.mapToRepository(lastUpdated.headers().get("Last-Modified").toString()) })

                } catch (e: IOException) {
                    val result =
                        response.body() ?: return@withContext Result.Error<List<FriendWrapper>>(RestError.EmptyBody)

                    return@withContext Result.Success(result.map { it.mapToRepository("0") })
                }

            } catch (e: IOException) {
                return@withContext Result.Error<List<FriendWrapper>>(RestError.NetworkError)
            }
        }

    override suspend fun getFollowers(username: String): Result<List<FriendWrapper>> =
        withContext(Dispatchers.IO) {
            try {
                val response = gitService.getFollowers(username).execute()

                if (!response.isSuccessful)
                    return@withContext Result.Error<List<FriendWrapper>>(
                        RestError.HttpError(
                            response.message(),
                            response.code()
                        )
                    )

                try {
                    val lastUpdated = gitService.getProfileHeader(username).execute()

                    if (!lastUpdated.isSuccessful) {
                        return@withContext Result.Error<List<FriendWrapper>>(
                            RestError.HttpError(
                                lastUpdated.message(),
                                lastUpdated.code()
                            )
                        )
                    }

                    val result =
                        response.body() ?: return@withContext Result.Error<List<FriendWrapper>>(RestError.EmptyBody)

                    return@withContext Result.Success(result.map { it.mapToRepository(lastUpdated.headers().get("Last-Modified").toString()) })

                } catch (e: IOException) {
                    val result =
                        response.body() ?: return@withContext Result.Error<List<FriendWrapper>>(RestError.EmptyBody)

                    return@withContext Result.Success(result.map { it.mapToRepository("0") })
                }

            } catch (e: IOException) {
                return@withContext Result.Error<List<FriendWrapper>>(RestError.NetworkError)
            }
        }


    override suspend fun getProfile(username: String): Result<ProfileWrapper> =
        withContext(Dispatchers.IO) {
            try {
                val response = gitService.getProfile(username).execute()

                if (!response.isSuccessful)
                    return@withContext Result.Error<ProfileWrapper>(
                        RestError.HttpError(
                            response.message(),
                            response.code()
                        )
                    )

                try {
                    val lastUpdated = gitService.getProfileHeader(username).execute()

                    if (!lastUpdated.isSuccessful) {
                        return@withContext Result.Error<ProfileWrapper>(
                            RestError.HttpError(
                                lastUpdated.message(),
                                lastUpdated.code()
                            )
                        )
                    }

                    val result =
                        response.body() ?: return@withContext Result.Error<ProfileWrapper>(RestError.EmptyBody)

                    return@withContext Result.Success(result.mapToRepository(lastUpdated.headers().get("Last-Modified").toString()))

                } catch (e: IOException) {
                    val result =
                        response.body() ?: return@withContext Result.Error<ProfileWrapper>(RestError.EmptyBody)

                    return@withContext Result.Success(result.mapToRepository("0"))
                }

            } catch (e: IOException) {
                return@withContext Result.Error<ProfileWrapper>(RestError.NetworkError)
            }
        }

    override suspend fun isDataUpToDate(username: String, dbTimeString: String): Boolean {
        var result = false

        withContext(Dispatchers.IO) {
            try {
                val response = gitService.getProfileHeader(username).execute()
                if (!response.isSuccessful) {
                    return@withContext false
                }

                val updatedTimeString = response.headers().get("Last-Modified").toString()
                result = dateComparator.isUpToDate(updatedTimeString, dbTimeString)
            } catch (e: IOException) {
                false
            }
        }

        return result
    }
}