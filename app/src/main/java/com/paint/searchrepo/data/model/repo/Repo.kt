package com.paint.searchrepo.data.model.repo

import com.google.gson.annotations.SerializedName

data class Repo(
    @field:SerializedName("full_name") var name: String,
    @field:SerializedName("owner") var owner: Owner,
    @field:SerializedName("description") var description: String
) {
    data class Owner(@field:SerializedName("avatar_url") var url: String)
}