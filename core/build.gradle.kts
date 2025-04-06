import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.mediaapp.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        buildConfigField("String", "API_KEY", getApiKey())
        buildConfigField("String", "FIREBASE_DATABASE_URL", getFirebaseURL())
        buildConfigField("String", "JAMENDO_API", getJamendoURL())
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core)

    implementation(libs.converter.gson)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.retrofit)

    implementation(libs.androidx.constraintlayout)
}

fun getApiKey(): String {
    val properties = Properties()
    file("../local.properties").inputStream().use { properties.load(it) }
    val key = properties.getProperty("API_KEY", "")
    return key
}

fun getFirebaseURL(): String {
    val properties = Properties()
    file("../local.properties").inputStream().use { properties.load(it) }
    return properties.getProperty("FIREBASE_DATABASE_URL", "")
}

fun getJamendoURL(): String {
    val properties = Properties()
    file("../local.properties").inputStream().use { properties.load(it) }
    return properties.getProperty("JAMENDO_API", "")
}