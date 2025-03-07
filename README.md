# Stripe Payment Test App

A sample Android application demonstrating Stripe payment integration using MVVM architecture and modern Android development practices.

## Features

- Stripe payment integration using Payment Sheet
- Secure API key management
- MVVM Architecture implementation
- Dependency Injection with Dagger Hilt
- Asynchronous programming with Kotlin Coroutines
- Network calls using Retrofit

## Technical Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger Hilt
- **Networking**: Retrofit
- **Asynchronous Programming**: Coroutines
- **Payment Integration**: Stripe Android SDK

## Stripe API Integration

This application demonstrates Stripe payment integration using the following APIs:

1. **Ephemeral Keys API**
   ```
   https://api.stripe.com/v1/ephemeral_keys
   ```
   Used for creating temporary authentication keys for the payment process.

2. **Payment Intents API**
   ```
   https://api.stripe.com/v1/payment_intents
   ```
   Used for creating and managing payment intents.

**Note**: Instead of using the Customers API (`https://api.stripe.com/v1/customers`), this example uses a pre-existing customer ID for demonstration purposes. In a production environment, you should implement proper customer management.

## Security

The application uses a `secure.properties` file to store sensitive information such as:
- Stripe Publishable Key
- Stripe Secret Key

This file is not committed to version control to maintain security.

## Project Structure

The project follows MVVM architecture with the following main components:

- **View**: Activities and Fragments
- **ViewModel**: Handles UI logic and state management
- **Repository**: Manages data operations
- **Network**: API service interfaces and implementations
- **DI**: Dependency injection modules

## Dependencies

- AndroidX Core and AppCompat
- Material Design Components
- Stripe Android SDK
- Retrofit for networking
- Kotlin Coroutines
- Dagger Hilt
- Lifecycle Components

## Setup

1. Clone the repository
2. Create a `secure.properties` file in the root directory
3. Add your Stripe API keys to the properties file:
   ```
   STRIPE_PUBLISHABLE_KEY=your_publishable_key
   STRIPE_SECRET_KEY=your_secret_key
   ```
4. Build and run the application