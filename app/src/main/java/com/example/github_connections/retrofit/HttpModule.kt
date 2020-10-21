package com.example.github_connections.retrofit

import com.example.github_connections.modules.util.GithubDateComparator
import com.example.github_connections.modules.util.DateComparator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val httpModule = Kodein.Module("HttpModule") {

    bind<OkHttpClient>() with singleton {
        OkHttpClient().newBuilder().addInterceptor {
            val original: Request = it.request()
            val requestBuilder: Request.Builder =
                original.newBuilder().header("Authorization", instance(tag = "token"))
            val request = requestBuilder.build()
            it.proceed(request)
        }.build()
    }

    bind<RestDataSource>() with singleton { RestDataSource(instance(), instance()) }

    bind<Retrofit>() with singleton {
        Retrofit.Builder()
            .client(instance())
            .baseUrl(instance<String>(tag = "BASE_URL"))
            .addConverterFactory( GsonConverterFactory.create(instance()))
            .build()
    }

    bind<Gson>() with singleton { GsonBuilder().create()}

    bind<GitService>() with singleton { instance<Retrofit>().create(GitService::class.java) }

    bind<DateComparator>() with singleton { GithubDateComparator() }

    constant("BASE_URL") with "https://api.github.com/"
    constant("token") with "token c765f3afc78d86d98543d5caba71a5fec1ba06d3"
}