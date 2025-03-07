import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.chami.strippaymenttest"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    val secureProperties = Properties()
    val securePropertiesFile = rootProject.file("secure.properties")
    if (securePropertiesFile.exists()) {
        secureProperties.load(FileInputStream(securePropertiesFile))
    }

    defaultConfig {
        applicationId = "com.chami.strippaymenttest"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        debug {
            buildConfigField("String", "STRIPE_PUBLISHABLE_KEY", "\"${secureProperties.getProperty("STRIPE_PUBLISHABLE_KEY")}\"")
            buildConfigField("String", "STRIPE_SECRET_KEY", "\"${secureProperties.getProperty("STRIPE_SECRET_KEY")}\"")         }
        release {
            buildConfigField("String", "STRIPE_PUBLISHABLE_KEY", "\"${secureProperties.getProperty("STRIPE_PUBLISHABLE_KEY")}\"")
            buildConfigField("String", "STRIPE_SECRET_KEY", "\"${secureProperties.getProperty("STRIPE_SECRET_KEY")}\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Stripe
    implementation(libs.stripe.android)
    implementation(libs.stripe.paymentsheet)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}