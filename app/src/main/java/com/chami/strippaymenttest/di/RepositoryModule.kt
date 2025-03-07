package com.chami.strippaymenttest.di

import com.chami.strippaymenttest.data.api.StripeApiService
import com.chami.strippaymenttest.data.repository.PaymentRepository
import com.chami.strippaymenttest.data.repository.PaymentRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePaymentRepository(stripeApiService: StripeApiService): PaymentRepository {
        return PaymentRepositoryImpl(stripeApiService)
    }
}