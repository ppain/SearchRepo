package com.paint.searchrepo.data.model.repo

import com.google.gson.annotations.SerializedName

class SearchRepo(
    @field:SerializedName("total_count") var count: Int,
    @field:SerializedName("incompleteResults") var incompleteResults: Boolean,
    @field:SerializedName("items") var items: List<Repo>
)