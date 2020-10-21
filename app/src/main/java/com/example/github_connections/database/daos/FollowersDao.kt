package com.example.github_connections.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.github_connections.database.entities.FollowersEntity

@Dao
interface FollowersDao {

    @Query("SELECT * FROM FollowersEntity WHERE user = (:username) COLLATE NOCASE")
    fun getFollowers(username: String): List<FollowersEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollower(followersEntity: FollowersEntity)

    @Query("DELETE FROM FollowersEntity WHERE user = (:username) COLLATE NOCASE")
    fun deleteUserFollowers(username: String)

    @Query("SELECT lastUpdated FROM FollowersEntity WHERE user = (:username) COLLATE NOCASE")
    fun getFollowersLastModified(username: String): String
}