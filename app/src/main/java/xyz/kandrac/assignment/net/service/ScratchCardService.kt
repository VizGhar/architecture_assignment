package xyz.kandrac.assignment.net.service

import retrofit2.http.GET
import xyz.kandrac.assignment.net.data.ScratchCardVersion

interface ScratchCardService {

    @GET("version")
    suspend fun getVersion(): ScratchCardVersion

}