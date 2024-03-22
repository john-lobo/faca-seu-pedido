plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.jlndev.baseservice"
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
    // OkHttp
    val okhttpVersion = "5.0.0-alpha.3"
    api("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    // Retrofit
    val retrofitVersion = "2.9.0"
    api("com.squareup.retrofit2:retrofit:$retrofitVersion")
    api("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    api("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")

    // Koin
    val koinVersion = "3.3.1"
    api("io.insert-koin:koin-android:$koinVersion")

    // RxJava
    val rxJavaVersion = "2.1.1"
    api("io.reactivex.rxjava2:rxandroid:$rxJavaVersion")

    //Gson
    val gsonVersion = "2.9.0"
    api ("com.google.code.gson:gson:$gsonVersion")

    //firebase
    api (platform("com.google.firebase:firebase-bom:31.2.3"))
    api ("com.google.firebase:firebase-analytics-ktx")
    api ("com.google.firebase:firebase-database-ktx")
    api ("com.google.firebase:firebase-storage-ktx")
    api ("com.google.firebase:firebase-auth-ktx")
    api ("com.google.android.gms:play-services-auth:20.5.0")
    api ("com.google.firebase:firebase-firestore-ktx")
}