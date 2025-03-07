package com.chami.strippaymenttest.data.model

data class PaymentIntentResponse(
    val id: String,
    val client_secret: String,
    val amount: Long,
    val currency: String,
    val status: String
)

data class AmountDetails(
    val tip: Map<String, Any>
)

data class AutomaticPaymentMethods(
    val allowRedirects: String,
    val enabled: Boolean
)

data class PaymentMethodConfigurationDetails(
    val id: String,
    val parent: String?
)

data class PaymentMethodOptions(
    val bancontact: BancontactOptions,
    val card: CardOptions,
    val eps: Map<String, Any>,
    val giropay: Map<String, Any>,
    val link: LinkOptions
)

data class BancontactOptions(
    val preferredLanguage: String
)

data class CardOptions(
    val installments: String?,
    val mandateOptions: String?,
    val network: String?,
    val requestThreeDSecure: String
)

data class LinkOptions(
    val persistentToken: String?
)