// Define versions at the top of your build.gradle.kts file
val room_version_kts = "2.6.1" // Using 'val' for a constant

plugins {
    alias(libs.plugins.android.application)
    // If you are using Kotlin for your app code (which is likely if using .kts for Gradle)
    // ensure you have the Kotlin Android plugin:
    // id("org.jetbrains.kotlin.android")
    // And for Room annotation processing with Kotlin:
    // id("kotlin-kapt") // Or id("com.google.devtools.ksp") for KSP
}

android {
    namespace = "com.example.blushfinance"
    compileSdk = 35 // Consider using a stable SDK version if 35 is a preview

    defaultConfig {
        applicationId = "com.example.blushfinance"
        minSdk = 24
        targetSdk = 35 // Consider using a stable SDK version if 35 is a preview
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
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
    //     jvmTarget = "11"
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

    // Room dependencies using Kotlin DSL string template
    implementation("androidx.room:room-runtime:$room_version_kts")
    // For Kotlin projects, if you use kapt for annotation processing:
    // kapt("androidx.room:room-compiler:$room_version_kts")
    // If you are using Java for your app code but .kts for gradle, annotationProcessor is still used:
    annotationProcessor("androidx.room:room-compiler:$room_version_kts")


    // Optional - Kotlin Extensions and Coroutines support for Room (if you use Kotlin)
    // implementation("androidx.room:room-ktx:$room_version_kts")

    // Optional - RxJava3 support for Room
    // implementation("androidx.room:room-rxjava3:$room_version_kts")

    // Optional - Test helpers
    // testImplementation("androidx.room:room-testing:$room_version_kts")
}
