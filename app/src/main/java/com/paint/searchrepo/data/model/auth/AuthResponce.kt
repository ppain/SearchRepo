package com.paint.searchrepo.data.model.auth

import com.google.gson.annotations.SerializedName

data class AuthResponce(
    @field:SerializedName("login") var login: String,
    @field:SerializedName("id") var id: String,
    @field:SerializedName("avatar_url") var avatarUrl: String,
    @field:SerializedName("public_repos") var publicRepos: Int,
    @field:SerializedName("total_private_repos") var totalPrivateRepos: Int
)