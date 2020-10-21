package com.example.github_connections.database

import android.app.Application
import androidx.room.Room
import org.kodein.di.Kodein
import org.kodein.di.generic.*

val databaseModule = Kodein.Module("DatabaseModule") {

    bind<DatabaseOperations>() with singleton { DatabaseDataSource(instance()) }

    bind<GithubDatabase>() with singleton {
        Room.databaseBuilder(
            instance<Application>().applicationContext,
            GithubDatabase::class.java,
            instance(tag = "DATABASE_NAME")
        ).build()
    }

    constant("DATABASE_NAME") with "github-connections"
}