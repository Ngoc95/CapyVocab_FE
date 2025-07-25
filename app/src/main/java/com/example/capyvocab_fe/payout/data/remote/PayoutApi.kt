package com.example.capyvocab_fe.payout.data.remote

import com.example.capyvocab_fe.auth.data.remote.model.GetAccountResponse
import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.payout.data.model.PayoutListResponse
import com.example.capyvocab_fe.payout.data.model.PayoutRequest
import com.example.capyvocab_fe.payout.data.model.UpdatePayoutRequest
import com.example.capyvocab_fe.payout.domain.model.Payout
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PayoutApi {
    @GET("auth/account")
    suspend fun getUserInfo(): ApiResponse<GetAccountResponse>
    @POST("payout")
    suspend fun createPayout(
        @Body request: PayoutRequest
    ): ApiResponse<Unit>

    @GET("payout")
    suspend fun getPayouts(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("email") email: String? = null,
        @Query("username") username: String? = null,
        @Query("amount") amount: Double? = null,
        @Query("status") status: String? = null
    ): ApiResponse<PayoutListResponse>

    @PUT("payout/{id}")
    suspend fun updatePayout(
        @Path("id") payoutId: Int,
        @Body body: UpdatePayoutRequest
    ): ApiResponse<Payout>
}