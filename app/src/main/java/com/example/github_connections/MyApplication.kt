package com.example.github_connections

import android.app.Application
import com.example.github_connections.database.databaseModule
import com.example.github_connections.modules.util.utilsModule
import com.example.github_connections.repository.repositoryModule
import com.example.github_connections.retrofit.httpModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class MyApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {

        bind<Application>() with provider { this@MyApplication }

        import(utilsModule)
        import(httpModule)
        import(databaseModule)
        import(repositoryModule)
    }
}