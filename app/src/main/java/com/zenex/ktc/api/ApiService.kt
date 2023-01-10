package com.zenex.ktc.api

import com.zenex.ktc.Constant
import com.zenex.ktc.api.param.input.ParamAssetBySite
import com.zenex.ktc.api.param.input.ParamEmpty
import com.zenex.ktc.api.param.input.ParamGetBreakdownItem
import java.util.concurrent.TimeUnit
import com.zenex.ktc.api.param.input.ParamLogin
import com.zenex.ktc.api.param.response.ParamGetAssetListResponse
import com.zenex.ktc.api.param.response.ParamGetBreakdownItemResponse
import com.zenex.ktc.api.param.response.ParamGetSiteListResponse
import com.zenex.ktc.api.param.response.ParamLoginResponse
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    fun doLogin(@Body paramLogin: ParamLogin?): Call<ParamLoginResponse>

    @POST("api/AssetIDBySite")
    fun getAssetId(@Body paramAssetBySite: ParamAssetBySite): Call<ParamGetAssetListResponse>

    @POST("api/SiteList")
    fun getSiteList(@Body paramEmpty: ParamEmpty): Call<ParamGetSiteListResponse>

    @POST("api/BreakdownEQM")
    fun getBreakdownItemList(@Body paramGetBreakdownItem: ParamGetBreakdownItem): Call<ParamGetBreakdownItemResponse>
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