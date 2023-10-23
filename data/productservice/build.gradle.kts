plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.jlndev.productservice"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://fakestoreapi.com/\"")
            buildConfigField("String", "DATABASE_NAME", "\"db_oder_history_database\"")
        }

        debug {
            buildConfigField("String", "BASE_URL", "\"https://fakestoreapi.com/\"")
            buildConfigField("String", "DATABASE_NAME", "\"db_oder_history_database_debug\"")
        }

        buildFeatures {
            buildConfig = true
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
    implementation(project(":data:baseservice"))

    // room
    val roomVersion = "2.6.0"
    implementation("androidx.room:room-rxjava2:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
}