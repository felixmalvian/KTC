package com.zenex.ktc.api

import com.zenex.ktc.Constant
import java.util.concurrent.TimeUnit
import com.zenex.ktc.api.param.input.ParamLogin
import com.zenex.ktc.api.param.response.ParamLoginResponse
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    fun doLogin(@Body paramLogin: ParamLogin?): Call<ParamLoginResponse>
}

private class UvCookieJar: CookieJar {
    private val cookies = mutableListOf<Cookie>()

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> = cookies

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies.clear()
        this.cookies.addAll(cookies)
    }

}


object RetrofitClient {
    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(UvCookieJar())
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(Constant.urlDev)
        .build()

    val instance: ApiService by lazy { retrofit.create(ApiService::class.java) }
}