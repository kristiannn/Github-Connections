package com.example.github_connections.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.github_connections.database.entities.RepositoryEntity

@Dao
interface RepositoryDao {

    @Query("SELECT * FROM RepositoryEntity WHERE user = (:username) COLLATE NOCASE")
    fun getRepos(username: String): List<RepositoryEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepo(repositoryEntity: RepositoryEntity)

    //These are needed, because a user may delete a repository or unfollow someone, which we will need to reflect as well
    @Query("DELETE FROM RepositoryEntity WHERE user = (:username) COLLATE NOCASE")
    fun deleteUserRepos(username: String)

    @Query("SELECT lastUpdated FROM RepositoryEntity WHERE user = (:username) COLLATE NOCASE")
    fun getReposLastModified(username: String): String
}