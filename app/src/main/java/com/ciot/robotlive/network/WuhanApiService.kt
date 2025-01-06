package com.ciot.robotlive.network

import com.ciot.robotlive.bean.RobotAllResponse
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

    /*获取网关 tcp端口*/
    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.DOMAIN_NAME_PROPERTY)
    @GET("/api/fittings/route")
    fun allow(): Observable<ResponseBody>

    /*根据项目id获取机器人列表*/
    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.DOMAIN_NAME_PROPERTY)
    @GET("/api/Robots/findByProject")
    fun findRobotByProject(@Query("access_token") token: String?,
                           @Query("projectid") projectId: String?,
                           @Query("start") start: String?,
                           @Query("limit") limit: String?,
    ): Observable<RobotAllResponse>

    /*机器人运动*/
    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.DOMAIN_NAME_PROPERTY)
    @POST("/api/Robots/ctrl/move")
    fun robotStartMove(@Query("access_token") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /*机器人停止运动*/
    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.DOMAIN_NAME_PROPERTY)
    @POST("/api/Robots/ctrl/stop")
    fun robotStopMove(@Query("access_token") token: String?,@Body body: RequestBody?): Observable<ResponseBody>
}
