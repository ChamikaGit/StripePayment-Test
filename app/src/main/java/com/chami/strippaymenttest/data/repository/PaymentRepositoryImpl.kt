package com.chami.strippaymenttest.data.repository

import android.util.Log
import com.chami.strippaymenttest.BuildConfig
import com.chami.strippaymenttest.data.api.StripeApiService
import com.chami.strippaymenttest.data.model.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val stripeApiService: StripeApiService
) : PaymentRepository {

    private val TAG = "PaymentRepositoryImpl"

    override suspend fun processPayment(
        amount: Int,
        currency: String
    ): ResultStatus<Pair<String, String>> = withContext(Dispatchers.IO) {
        try {
            if (!validatePaymentDetails(amount, currency)) {
                return@withContext ResultStatus.Error("Invalid payment details")
            }

            val ephemeralKeyResponse = stripeApiService.createEphemeralKey(customerId = "cus_RtU9pv8viAIODb")
            if (!ephemeralKeyResponse.isSuccessful) {
                return@withContext ResultStatus.Error(
                    "Failed to create ephemeral key: ${ephemeralKeyResponse.errorBody()?.string() ?: "Unknown error"}"
                )
            }

            val ephemeralKey = ephemeralKeyResponse.body() ?: return@withContext ResultStatus.Error("Empty response while creating ephemeral key")
            val ephemeralKeySecret = ephemeralKey.secret

            val paymentIntentResponse = stripeApiService.createPaymentIntent(
                amount = amount,
                currency = currency.lowercase(),
                customerId = "cus_RtU9pv8viAIODb"
            )

            if (!paymentIntentResponse.isSuccessful) {
                return@withContext ResultStatus.Error(
                    "Failed to create payment intent: ${paymentIntentResponse.errorBody()?.string() ?: "Unknown error"}"
                )
            }

            val paymentIntent = paymentIntentResponse.body() ?: return@withContext ResultStatus.Error("Empty response while creating payment intent")

            if(BuildConfig.DEBUG) {
                Log.e(
                    TAG,
                    "processPayment: ephemeralKeySecret: ${ephemeralKeySecret} paymentIntent: ${paymentIntent.client_secret}"
                )
            }

            ResultStatus.Success(Pair(ephemeralKeySecret, paymentIntent.client_secret))
        } catch (e: Exception) {
            ResultStatus.Error("Payment processing failed: ${e.message ?: "Unknown error"}")
        }
    }

    override suspend fun validatePaymentDetails(amount: Int, currency: String): Boolean {
        return amount > 0 && currency.isNotEmpty() && currency.length == 3
    }
}
