import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

kotlin{
    sourceSets{
        debug{
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release{
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}
android {
    namespace = "com.example.opencontrol"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.opencontrol"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //Core
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.compose.material:material:1.3.1")

    //Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    //Koin
    val koinVersion = "3.4.0"
    implementation ("io.insert-koin:koin-android:$koinVersion")
    implementation ("io.insert-koin:koin-androidx-compose:$koinVersion")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    //Voyager
    val voyagerVersion = "1.0.0-rc05"
    implementation ("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation ("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
    implementation ("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
    implementation ("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")

    //Agora
    implementation ("com.github.AgoraIO-Community.VideoUIKit-Android:final:v4.0.0")

    //Retrofit
    val retrofitVersion = "2.9.0"
    implementation ("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //Coil
    implementation ("io.coil-kt:coil-compose:2.2.2")

    //Room
    val room_version = "2.5.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    //Compose
    val compose = "1.4.3"
    implementation ("androidx.compose.material:material-icons-extended:$compose")
    implementation (platform("androidx.compose:compose-bom:2022.10.00"))
    implementation ("androidx.compose.ui:ui:$compose")
    implementation ("androidx.compose.ui:ui-graphics")
    implementation ("androidx.compose.material:material:$compose")
    implementation ("androidx.compose.ui:ui-tooling-preview:$compose")
    implementation ("androidx.compose.material3:material3")

    //Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}