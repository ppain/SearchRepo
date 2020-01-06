package com.paint.searchrepo.ui.base.repo

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paint.searchrepo.data.Repository
import com.paint.searchrepo.data.model.repo.Repo
import com.paint.searchrepo.data.model.repo.SearchRepo
import com.paint.searchrepo.data.network.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //    Events
    private val _navigateToAuth = MutableLiveData<Boolean>()
    val navigateToAuth: LiveData<Boolean>
        get() = _navigateToAuth

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun navigateToAuthHandled() {
        _navigateToAuth.value = false
    }

    fun messageHandled() {
        _message.value = ""
    }


    //    param loading
    private var _query = ""
    val query: String
        get() = _query
    private var page: Int = 0
    private var amountRepo = 0

    var isLoading = ObservableBoolean()

    private var lastList: List<Repo> = mutableListOf()
    private val _repoList = MutableLiveData<List<Repo>>().also {
        if (repository.getPrefBasicToken()!!.isNotEmpty()) {
            amountRepo = repository.getPrefTotalRepo()
            newRepoRequest("")
        }
    }

    fun repoList() = _repoList as LiveData<List<Repo>>

    fun newRepoRequest(query: String) {
        _query = query
        page = 1
        lastList = mutableListOf()
        fetchRepo(page)
    }

    fun checkNeedGetRepo(visibleItemCount: Int, pastVisibleItem: Int, total: Int) {
        if (!isLoading.get() && lastList.size < amountRepo && (visibleItemCount + pastVisibleItem) >= total) {
            page++
            fetchRepo(page)
        }
    }

    private fun fetchRepo(page: Int) {
        isLoading.set(true)
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                if (query.isNotEmpty()) {
                    repository.getRepo(_query, page)
                } else {
                    repository.getUserRepo(page)
                }) {
                is ApiResponse.Success -> {
                    val body = response.body
                    if (body is SearchRepo) {
                        amountRepo = body.count
                        updateRepoList(body.items)
                    } else {
                        updateRepoList(body as List<Repo>)
                    }
                }
                is ApiResponse.Failure -> {
                    _message.postValue(response.message)
                    isLoading.set(false)
                }
            }
        }
    }

    private fun updateRepoList(list: List<Repo>) {
        lastList = if (lastList.isEmpty()) {
            list
        } else {
            lastList.plus(list)
        }
        _repoList.postValue(lastList)
        isLoading.set(false)
    }

    fun checkAuth(): Boolean {
        return repository.getPrefBasicToken()!!.isNotEmpty()
    }

    fun actionAuth() {
        repository.clearPrefBasicToken()
        _navigateToAuth.postValue(true)
    }
}