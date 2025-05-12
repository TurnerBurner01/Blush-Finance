// Top-level definition for versions (good practice)
val room_version_kts = "2.6.1"

plugins {
    alias(libs.plugins.android.application)
    // If your app code is Kotlin (likely):
    // id("org.jetbrains.kotlin.android")
    // If using Room with Kotlin, you'll need kapt or ksp for annotation processing:
    // id("kotlin-kapt")
    // or
    // id("com.google.devtools.ksp") // if using KSP
}

android {
    namespace = "com.example.blushfinance"
    compileSdk = 35 // Consider using a stable SDK version if 35 is a preview (e.g., 34)

    defaultConfig {
        applicationId = "com.example.blushfinance"
        minSdk = 24
        targetSdk = 35 // Consider using a stable SDK version if 35 is a preview (e.g., 34)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Proguard is not run if minifyEnabled is false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // This line is standard
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // If using Kotlin in your app:
    // kotlinOptions {
    //     jvmTarget = "11" // Or your desired JVM target for Kotlin
    // }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Room dependencies
    implementation("androidx.room:room-runtime:$room_version_kts")
    // If app code is Java:
    annotationProcessor("androidx.room:room-compiler:$room_version_kts")
    // If app code is Kotlin and using kapt:
    // kapt("androidx.room:room-compiler:$room_version_kts")
    // If app code is Kotlin and using KSP:
    // ksp("androidx.room:room-compiler:$room_version_kts")


    implementation("com.github.KwabenBerko:News-API-Java:1.0.2")
    implementation("com.squareup.picasso:picasso:2.8")

    // OkHttp Logging Interceptor (if you need it, ensure you also have OkHttp)
    // implementation("com.squareup.okhttp3:logging-interceptor:4.9.3") // Example version
    // implementation("com.squareup.okhttp3:okhttp:4.9.3") // Example version for OkHttp itself
}
