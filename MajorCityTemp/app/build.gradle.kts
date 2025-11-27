plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.majorcitytemp"
    compileSdk = 36

    // *** 关键修正 1: 启用 BuildConfig 功能 ***
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.majorcitytemp"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // *** 关键修正 2: 保持 API Key 配置在唯一的 defaultConfig 块中 ***
        // 这里的密钥必须是您从 OpenWeatherMap 获取的有效密钥，
        // 否则您将持续收到 401 错误。
        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"7654e6be5693472eb47299007d6536ff\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 添加网络和 JSON 库
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // 协程支持
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}