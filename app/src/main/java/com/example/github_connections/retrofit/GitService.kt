package com.example.github_connections.retrofit

import com.example.github_connections.retrofit.dto.FriendsDto
import com.example.github_connections.retrofit.dto.ProfileDto
import com.example.github_connections.retrofit.dto.RepositoryDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitService {

    @GET("users/{username}")
    fun getProfile(@Path("username") username: String): Call<ProfileDto>

    @GET("users/{username}/repos")
    fun getRepos(@Path("username") username: String): Call<List<RepositoryDto>>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<FriendsDto>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<FriendsDto>>

    @GET("users/{username}")
    fun getProfileHeader(@Path("username") username: String) : Call<Void>
}