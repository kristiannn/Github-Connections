package com.example.github_connections.repository

import com.example.github_connections.Result
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper

/* We are assuming that everything from external frameworks (Room & Retrofit in this case) works flawlessly
    So we are mocking the results with a fake/custom ones, without going further and implementing
    a fake Retrofit or Room service */

class CachingFakeData : GithubRepository {

    override suspend fun getRepos(username: String): Result<List<RepositoryWrapper>> {
        val repositories: MutableList<RepositoryWrapper> = mutableListOf()
        repositories.add(
            RepositoryWrapper(
                "Fake Rep 1",
                "Fake Description 1",
                4,
                5,
                "0"
            )
        )
        repositories.add(
            RepositoryWrapper(
                "Fake Rep 2",
                "Fake Description 2",
                6,
                4,
                "0"
            )
        )
        repositories.add(
            RepositoryWrapper(
                "Fake Rep 3",
                "Fake Description 3",
                2,
                7,
                "0"
            )
        )
        repositories.add(
            RepositoryWrapper(
                "Fake Rep 4",
                "Fake Description 4",
                3,
                3,
                "0"
            )
        )

        return Result.Success(repositories.toList())
    }

    override suspend fun getFollowing(username: String): Result<List<FriendWrapper>> {
        val following: MutableList<FriendWrapper> = mutableListOf()
        following.add(
            FriendWrapper(
                "Friend 1",
                "http://blablabla.com",
                "0"
            )
        )
        following.add(
            FriendWrapper(
                "Friend 2",
                "http://blablabla.com",
                "0"
            )
        )
        following.add(
            FriendWrapper(
                "Friend 3",
                "http://blablabla.com",
                "0"
            )
        )
        following.add(
            FriendWrapper(
                "Friend 4",
                "http://blablabla.com",
                "0"
            )
        )

        return Result.Success(following.toList())
    }

    override suspend fun getFollowers(username: String): Result<List<FriendWrapper>> {
        val followers: MutableList<FriendWrapper> = mutableListOf()
        followers.add(
            FriendWrapper(
                "Friend 1",
                "http://blablabla.com",
                "0"
            )
        )
        followers.add(
            FriendWrapper(
                "Friend 2",
                "http://blablabla.com",
                "0"
            )
        )
        followers.add(
            FriendWrapper(
                "Friend 3",
                "http://blablabla.com",
                "0"
            )
        )
        followers.add(
            FriendWrapper(
                "Friend 4",
                "http://blablabla.com",
                "0"
            )
        )

        return Result.Success(followers.toList())
    }

    override suspend fun getProfile(username: String): Result<ProfileWrapper> {
        if (username != "Fail Profile")
            return Result.Success(
                ProfileWrapper(
                    "Test Profile 1",
                    "http://blaaaaaaaaaa.com",
                    "Test Biography 1",
                    "Test Location",
                    1,
                    1,
                    1,
                    "0"
                )
            )
        else {
            return Result.Error(Throwable(message = "Test error"))
        }
    }
}