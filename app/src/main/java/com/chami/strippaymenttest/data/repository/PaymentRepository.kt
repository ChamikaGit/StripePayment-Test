package com.chami.strippaymenttest.data.repository

import com.chami.strippaymenttest.data.model.EphemeralKeyResponse
import com.chami.strippaymenttest.data.model.PaymentIntentResponse
import com.chami.strippaymenttest.data.model.ResultStatus

interface PaymentRepository {
    suspend fun processPayment(amount: Int, currency: String): ResultStatus<Pair<String, String>>
    suspend fun validatePaymentDetails(amount: Int, currency: String): Boolean
//    suspend fun createEphemeralKey(customerId: String): ResultStatus<EphemeralKeyResponse>
}