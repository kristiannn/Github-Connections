package com.example.github_connections.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.github_connections.database.entities.FollowingEntity

@Dao
interface FollowingDao {

    @Query("SELECT * FROM FollowingEntity WHERE user = (:username) COLLATE NOCASE")
    fun getFollowing(username: String): List<FollowingEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowing(followingEntity: FollowingEntity)

    @Query("DELETE FROM FollowingEntity WHERE user = (:username) COLLATE NOCASE")
    fun deleteUserFollowings(username: String)

    @Query("SELECT lastUpdated FROM FollowingEntity WHERE user = (:username) COLLATE NOCASE")
    fun getFollowingLastModified(username: String): String
}