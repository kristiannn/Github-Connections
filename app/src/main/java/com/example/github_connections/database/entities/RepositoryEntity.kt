package com.example.github_connections.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.github_connections.repository.models.RepositoryWrapper

@Entity(
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("user")
    )]
)
data class RepositoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val user: String,
    val name: String,
    val description: String,
    val watchersCount: Int,
    val forksCount: Int,
    @ColumnInfo(name = "lastUpdated")
    val lastUpdated: String
)

fun RepositoryEntity.mapToRepository(): RepositoryWrapper {
    return RepositoryWrapper(
        name,
        description,
        watchersCount,
        forksCount,
        lastUpdated
    )
}