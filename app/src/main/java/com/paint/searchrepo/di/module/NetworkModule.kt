package com.paint.searchrepo.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paint.searchrepo.data.Repository
import com.paint.searchrepo.data.network.Api
import com.paint.searchrepo.di.module.interceptor.NetworkInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRepository(api: Api): Repository {
        return Repository(api)
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: NetworkInterceptor,
        certs: Array<TrustManager>,
        hostNameVerifier: HostnameVerifier
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)

        var ctx: SSLContext? = null

        try {
            ctx = SSLContext.getInstance("TLS")
            ctx.init(null, certs, SecureRandom())
        } catch (ex: GeneralSecurityException) {

        }

        try {
            ctx?.let {
                builder.hostnameVerifier(hostNameVerifier)
                    .sslSocketFactory(it.socketFactory, certs[0] as X509TrustManager)
            }
        } catch (ex: Exception) {
        }

        builder.addInterceptor(httpLoggingInterceptor)
            .addInterceptor(networkInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideTrustManager(trustManager: X509TrustManager): Array<TrustManager> {
        return arrayOf(trustManager)
    }

    @Provides
    @Singleton
    fun provideX509TrustManager(): X509TrustManager {
        return object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }
        }
    }

    @Provides
    @Singleton
    fun provideHostnameVerifier(): HostnameVerifier {
        return object : HostnameVerifier {
            override fun verify(hostname: String?, session: SSLSession?): Boolean {
                return if ("www.asdasdad.com".equals(hostname, true)) false else true
            }
        }
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(): NetworkInterceptor {
        return NetworkInterceptor()
    }
}