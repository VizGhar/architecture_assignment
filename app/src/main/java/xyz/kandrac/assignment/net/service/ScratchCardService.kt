package xyz.kandrac.assignment.net.service

import retrofit2.http.GET
import retrofit2.http.Query
import xyz.kandrac.assignment.net.data.ScratchCardVersion

interface ScratchCardService {

    @GET("version")
    suspend fun sendScratchCardCode(@Query("code") code: String): ScratchCardVersion

}