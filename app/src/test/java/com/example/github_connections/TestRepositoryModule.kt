package com.example.github_connections

import com.example.github_connections.repository.CachingFakeData
import com.example.github_connections.repository.GithubRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val testRepositoryModule = Kodein.Module("TestRepositoryModule") {

    bind<GithubRepository>() with singleton {
        CachingFakeData()
    }
}