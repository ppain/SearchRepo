package com.paint.searchrepo.data.network

import com.paint.searchrepo.data.model.auth.AuthResponce
import com.paint.searchrepo.data.model.repo.Repo
import com.paint.searchrepo.data.model.repo.SearchRepo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Api {
    @GET("user")
    suspend fun logIn(
        @Header("Authorization") token: String?
    ): Response<AuthResponce>

    @GET("user/repos")
    suspend fun getUserRepo(
        @Header("Authorization") token: String?,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("sort") sort: String
    ): Response<List<Repo>>

    @GET("search/repositories")
    suspend fun getRepo(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("sort") sort: String
    ): Response<SearchRepo>
}