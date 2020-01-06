package com.paint.searchrepo.ui.base.auth

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paint.searchrepo.data.Repository
import com.paint.searchrepo.data.model.auth.AuthResponce
import com.paint.searchrepo.data.network.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Credentials
import javax.inject.Inject


class AuthViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //    Events
    private val _navigateToRepo = MutableLiveData<Boolean>()
    val navigateToRepo: LiveData<Boolean>
        get() = _navigateToRepo

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun navigateToRepoHandled() {
        _navigateToRepo.value = false
    }

    fun messageHandled() {
        _message.value = ""
    }

    var login = ObservableField<String>()
    var password = ObservableField<String>()
    var isLoading = ObservableBoolean()

    private lateinit var authToken: String


    fun checkLastAuth() {
        if (repository.getPrefBasicToken()!!.isNotEmpty()) {
            _navigateToRepo.postValue(true)
        }
    }

    fun login() {
        if (!login.get().isNullOrEmpty() && !password.get().isNullOrEmpty()) {
            isLoading.set(true)
            authToken = Credentials.basic(login.get()!!, password.get()!!)
            CoroutineScope(Dispatchers.IO).launch {
                when (val response = repository.logIn(authToken)) {
                    is ApiResponse.Success -> {
                        onLogInResponse(response.body!!)
                        isLoading.set(false)
                    }
                    is ApiResponse.Failure -> {
                        _message.postValue(response.message)
                        isLoading.set(false)
                    }
                }
            }
        }
    }

    //    save auth param and go to baseActivity
    private fun onLogInResponse(auth: AuthResponce) {
        repository.setPrefBasicToken(authToken)
        repository.setPrefTotalRepo(auth)
        _navigateToRepo.postValue(true)
    }

    fun enterWithoutAuth() {
        _navigateToRepo.postValue(true)
    }
}