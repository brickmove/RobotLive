package com.ciot.robotlive.network

import io.reactivex.Observable
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface WuhanApiService {
    /*登录*/
    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.DOMAIN_NAME_PROPERTY)
    @POST("/api/Users/md5Login")
    fun md5Login(@Body body: RequestBody): Observable<ResponseBody>

    /*获取网关*/
    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.DOMAIN_NAME_PROPERTY)
    @GET("/api/fittings/route")
    fun allow(): Observable<ResponseBody>


}
