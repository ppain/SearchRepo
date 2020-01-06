package com.paint.searchrepo.di.module.interceptor

import android.content.Context
import android.net.ConnectivityManager
import com.paint.searchrepo.App
import com.paint.searchrepo.util.Const
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoConnectivityException(Const.ERROR_NO_INTERNET_CONNECTION)
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    inner class NoConnectivityException(message: String?) : IOException(message) {
        override val message: String
            get() = Const.ERROR_NO_INTERNET_CONNECTION
    }

    companion object {
        val isConnected: Boolean
            get() {
                val connectivityManager = App.instance
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = connectivityManager.activeNetworkInfo
                return netInfo != null && netInfo.isConnected
            }
    }
}