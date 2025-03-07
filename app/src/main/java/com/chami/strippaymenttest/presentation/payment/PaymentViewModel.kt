package com.chami.strippaymenttest.presentation.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chami.strippaymenttest.data.model.ResultStatus
import com.chami.strippaymenttest.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {
    private val _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> = _paymentState

    data class ValidationResult(
        val isValid: Boolean,
        val orderNumberError: String? = null,
        val amountError: String? = null
    )

    private val _validationState = MutableLiveData<ValidationResult>()
    val validationState: LiveData<ValidationResult> = _validationState

    fun validatePaymentDetails(orderNumber: String, amount: String): Boolean {
        if (orderNumber.isBlank()) {
            _validationState.value = ValidationResult(
                isValid = false,
                orderNumberError = "Please enter order number"
            )
            return false
        }

        if (amount.isBlank()) {
            _validationState.value = ValidationResult(
                isValid = false,
                amountError = "Please enter amount"
            )
            return false
        }

        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _validationState.value = ValidationResult(
                isValid = false,
                amountError = "Please enter a valid amount"
            )
            return false
        }

        _validationState.value = ValidationResult(isValid = true)
        return true
    }

//    fun initializePaymentSheet() = liveData {
//        try {
//            val customerId = "cus_RtU9pv8viAIODb" // Replace with dynamic customer ID in production
//            val ephemeralKeyResult = paymentRepository.createEphemeralKey(customerId)
//
//            ephemeralKeyResult.fold(
//                onSuccess = { ephemeralKey ->
//                    emit(PaymentSheetInitResult.Success(
//                        customerId = customerId,
//                        ephemeralKey = ephemeralKey["secret"] as String,
//                        paymentIntent = "", // Will be set when processing payment
//                        publishableKey = "pk_test_TYooMQauvdEDq54NiTphI7jx" // Replace with your publishable key
//                    ))
//                },
//                onFailure = { error ->
//                    emit(PaymentSheetInitResult.Error(error.message ?: "Failed to initialize payment sheet"))
//                }
//            )
//        } catch (e: Exception) {
//            emit(PaymentSheetInitResult.Error(e.message ?: "Failed to initialize payment sheet"))
//        }
//    }

    fun processPayment(amount: Int, currency: String) {
        viewModelScope.launch {
            if (!paymentRepository.validatePaymentDetails(amount, currency)) {
                _paymentState.value = PaymentState.Error("Invalid payment details")
                return@launch
            }

            _paymentState.value = PaymentState.Loading

            when (val result = paymentRepository.processPayment(amount, currency)) {
                is ResultStatus.Success -> {
                    val (ephemeralKeySecret, paymentIntentClientSecret) = result.data
                    if (paymentIntentClientSecret.isNullOrBlank() || ephemeralKeySecret.isNullOrBlank()) {
                        _paymentState.value = PaymentState.Error("Invalid payment response")
                        return@launch
                    }
                    _paymentState.value = PaymentState.Success(
                        clientSecret = paymentIntentClientSecret,
                        ephemeralKeySecret = ephemeralKeySecret,
                        publishableKey = com.chami.strippaymenttest.BuildConfig.STRIPE_PUBLISHABLE_KEY
                    )
                }
                is ResultStatus.Error -> {
                    _paymentState.value = PaymentState.Error(result.message)
                }
                is ResultStatus.Loading -> PaymentState.Loading
            }
        }
    }

    sealed class PaymentState {
        object Loading : PaymentState()
        data class Success(
            val clientSecret: String,
            val ephemeralKeySecret: String,
            val publishableKey: String
        ) : PaymentState()
        data class Error(val message: String) : PaymentState()
    }

//    fun handlePaymentSuccess() {
//        _paymentState.value = PaymentState.Success(
//            clientSecret = "",
//            publishableKey = ""
//        )
//    }
//
//    fun handlePaymentCancellation() {
//        _paymentState.value = PaymentState.Error("Payment cancelled")
//    }
//
//    fun handlePaymentError(error: Throwable) {
//        _paymentState.value = PaymentState.Error(error.message ?: "Payment failed")
//    }
//
//    sealed class PaymentSheetInitResult {
//        data class Success(
//            val customerId: String,
//            val ephemeralKey: String,
//            val paymentIntent: String,
//            val publishableKey: String
//        ) : PaymentSheetInitResult()
//        data class Error(val message: String) : PaymentSheetInitResult()
//    }
}