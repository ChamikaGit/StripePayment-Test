package com.chami.strippaymenttest.data.api

import com.chami.strippaymenttest.data.model.EphemeralKeyResponse
import com.chami.strippaymenttest.data.model.PaymentIntentResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Header

interface StripeApiService {
    @POST("payment_intents")
    suspend fun createPaymentIntent(
        @Query("amount") amount: Int,
        @Query("currency") currency: String,
        @Query("customer") customerId: String
    ): Response<PaymentIntentResponse>

    @POST("ephemeral_keys")
    suspend fun createEphemeralKey(
        @Header("Stripe-Version") stripeVersion: String = "2025-02-24.acacia",
        @Query("customer") customerId: String
    ): Response<EphemeralKeyResponse>
}