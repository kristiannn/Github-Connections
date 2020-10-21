package com.example.github_connections.repository

import com.example.github_connections.modules.CachingDataSource
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val repositoryModule = Kodein.Module("RepositoryModule") {

    bind<GithubRepository>() with singleton {
        CachingDataSource(instance(), instance(), instance())
    }
}