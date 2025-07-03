package com.example.capyvocab_fe.user.payment.data.remote

import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.user.payment.data.remote.model.CreateOrderResponse
import com.example.capyvocab_fe.user.payment.domain.model.Order
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface PaymentApi {
    @POST("exercise/{id}/new-order")
    suspend fun createOrder(
        @Path("id") folderId: Int
    ): ApiResponse<CreateOrderResponse>

    @GET("orders/vnpay-return")
    suspend fun handleVnpayReturn(
        @QueryMap params: Map<String, String>
    ): ApiResponse<Order>

    @GET("exercise/{id}/order-status")
    suspend fun checkOrderStatus(
        @Path("id") folderId: Int
    ): ApiResponse<Order?>

    @DELETE("orders/{orderId}")
    suspend fun cancelOrder(
        @Path("orderId") orderId: String
    ): ApiResponse<Unit>
}