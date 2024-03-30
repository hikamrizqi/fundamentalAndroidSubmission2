package com.hikam.hikamfundamentalandroidsubmission2.data.retrofit

import com.hikam.hikamfundamentalandroidsubmission2.data.response.DetailUserResponse
import com.hikam.hikamfundamentalandroidsubmission2.data.response.GithubResponse
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}