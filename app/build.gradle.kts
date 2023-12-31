plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "dev.davron.regionaltaxi"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.davron.regionaltaxi"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.2")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //mask edittext
    implementation("com.github.santalu:maskara:1.0.0")


//    lotti animation
    implementation("com.airbnb.android:lottie:5.0.3")
//
//    // Dependency on local binaries
//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//
//    // Dependency on a remote binary
//    implementation("com.example.android:app-magic:12.3")
    implementation("ru.egslava:MaskedEditText:1.0.5")

    ///sms input layout...
    implementation("com.github.mukeshsolanki:android-otpview-pinview:2.1.2")

    //shimmer layout
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //karumi dexter
    implementation("com.karumi:dexter:6.2.3")

    //rounded corner image view
    implementation("com.makeramen:roundedimageview:2.3.0")

    //calendar view
    implementation ("com.github.prolificinteractive:material-calendarview:2.0.1")

}