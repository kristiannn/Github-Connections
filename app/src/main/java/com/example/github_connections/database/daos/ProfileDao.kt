package com.example.github_connections.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.github_connections.database.entities.ProfileEntity

@Dao
interface ProfileDao {

    @Query("SELECT * FROM ProfileEntity WHERE name = (:username) COLLATE NOCASE")
    fun getProfile(username: String): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profileEntity: ProfileEntity)

    @Query("SELECT lastUpdated FROM ProfileEntity WHERE name = (:username) COLLATE NOCASE")
    fun getProfileLastModified(username: String): String
}