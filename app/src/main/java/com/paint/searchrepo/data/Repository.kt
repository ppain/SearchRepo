package com.paint.searchrepo.data

import com.paint.searchrepo.data.model.auth.AuthResponce
import com.paint.searchrepo.data.model.repo.Repo
import com.paint.searchrepo.data.model.repo.SearchRepo
import com.paint.searchrepo.data.network.Api
import com.paint.searchrepo.data.network.ApiResponse
import com.paint.searchrepo.util.Const
import com.paint.searchrepo.util.PreferenceUtil.getPreferenceInt
import com.paint.searchrepo.util.PreferenceUtil.getPreferenceString
import com.paint.searchrepo.util.PreferenceUtil.removePreferenceString
import com.paint.searchrepo.util.PreferenceUtil.setPreferenceInt
import com.paint.searchrepo.util.PreferenceUtil.setPreferenceString
import retrofit2.Response
import javax.inject.Inject


class Repository @Inject constructor(val api: Api) {

    //    preference
    fun setPrefBasicToken(token: String) {
        setPreferenceString(Const.PREF_KEY_BASIC_TOKEN, token)
    }

    fun setPrefTotalRepo(auth: AuthResponce) {
        setPreferenceInt(Const.PREF_KEY_TOTAL_REPO, (auth.publicRepos + auth.totalPrivateRepos))
    }

    fun clearPrefBasicToken() {
        removePreferenceString(Const.PREF_KEY_BASIC_TOKEN)
    }

    fun getPrefBasicToken(): String? {
        return getPreferenceString(Const.PREF_KEY_BASIC_TOKEN)
    }

    fun getPrefTotalRepo(): Int {
        return getPreferenceInt(Const.PREF_KEY_TOTAL_REPO)
    }


    //    Api
    private fun <T> responseApi(response: Response<T>): ApiResponse<T?> {
        return if (response.isSuccessful)
            ApiResponse.Success(response.body())
        else
            ApiResponse.Failure(response.message(), response.code())
    }

    suspend fun logIn(token: String): ApiResponse<AuthResponce?> {
        return responseApi(api.logIn(token))
    }

    suspend fun getUserRepo(page: Int): ApiResponse<List<Repo>?> {
        return responseApi(
            api.getUserRepo(
                getPrefBasicToken(),
                Const.REPO_TYPE,
                page,
                Const.COUNT_ITEM_PER_PAGE,
                Const.SORT_TYPE
            )
        )
    }

    suspend fun getRepo(query: String, page: Int): ApiResponse<SearchRepo?> {
        return responseApi(
            api.getRepo(
                query,
                page,
                Const.COUNT_ITEM_PER_PAGE,
                Const.SORT_TYPE
            )
        )
    }
}