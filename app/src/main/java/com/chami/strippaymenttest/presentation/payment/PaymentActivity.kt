package com.chami.strippaymenttest.presentation.payment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.chami.strippaymenttest.databinding.ActivityPaymentBinding
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private val viewModel: PaymentViewModel by viewModels()
    private lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.paymentState.observe(this) { state ->
            when (state) {
                is PaymentViewModel.PaymentState.Loading -> {
                    binding.payButton.isEnabled = false
                    binding.paymentProgress.visibility = android.view.View.VISIBLE
                }

                is PaymentViewModel.PaymentState.Success -> {
                    binding.paymentProgress.visibility = android.view.View.INVISIBLE
                    showPaymentSheet(state)
                }

                is PaymentViewModel.PaymentState.Error -> {
                    binding.payButton.isEnabled = true
                    binding.paymentProgress.visibility = android.view.View.INVISIBLE
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.validationState.observe(this) { validationResult ->
            binding.orderNumberInputLayout.error = validationResult.orderNumberError
            binding.amountInputLayout.error = validationResult.amountError
        }
    }

    private fun setupClickListeners() {
        binding.payButton.setOnClickListener {
            val orderNumber = binding.orderNumberInput.text.toString()
            val amount = binding.amountInput.text.toString()
            if (viewModel.validatePaymentDetails(orderNumber, amount)) {
                viewModel.processPayment(amount.toInt(), "USD")
            }
        }
    }

    private fun showPaymentSheet(state: PaymentViewModel.PaymentState.Success) {
        customerConfig = PaymentSheet.CustomerConfiguration(
            id = "cus_RtU9pv8viAIODb",
            ephemeralKeySecret = state.ephemeralKeySecret
        )
        PaymentConfiguration.init(this, state.publishableKey)

        paymentSheet.presentWithPaymentIntent(
            state.clientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Xiteb",
                customer = customerConfig,
            )
        )
    }

    private fun onPaymentSheetResult(result: PaymentSheetResult) {
        when (result) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this@PaymentActivity, "Payment Completed!", Toast.LENGTH_SHORT).show()
            }

            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this@PaymentActivity, "Payment Canceled", Toast.LENGTH_SHORT).show()

            }

            is PaymentSheetResult.Failed -> {
                Toast.makeText(this@PaymentActivity, "Payment Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }
}