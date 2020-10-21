package com.example.github_connections.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github_connections.database.daos.FollowersDao
import com.example.github_connections.database.daos.FollowingDao
import com.example.github_connections.database.daos.ProfileDao
import com.example.github_connections.database.daos.RepositoryDao
import com.example.github_connections.database.entities.FollowersEntity
import com.example.github_connections.database.entities.FollowingEntity
import com.example.github_connections.database.entities.ProfileEntity
import com.example.github_connections.database.entities.RepositoryEntity

@Database(
    entities = [
        ProfileEntity::class,
        RepositoryEntity::class,
        FollowingEntity::class,
        FollowersEntity::class],
    version = 1
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun dbDAOProfile(): ProfileDao
    abstract fun dbDAOFollowing(): FollowingDao
    abstract fun dbDAOFollowers(): FollowersDao
    abstract fun dbDAORepository(): RepositoryDao
}