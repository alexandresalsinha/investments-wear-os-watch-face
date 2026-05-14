plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mywatchface"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.mywatchface"
        minSdk = 36
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.watchface.complications.data)
    implementation(libs.androidx.watchface.complications.datasource)
    implementation(libs.androidx.watchface.complications.datasource.ktx)
    implementation(libs.kotlinx.coroutines.android)
}
