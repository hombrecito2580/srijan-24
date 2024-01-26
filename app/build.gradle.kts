import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}



android {
    namespace = "com.iitism.srijan24"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iitism.srijan24"
        minSdk = 24
        targetSdk = 34
        versionCode = 7
        versionName = "7.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        buildConfigField("String", "RAZORPAY_KEY", "\"${properties.getProperty("RAZORPAY_KEY")}\"")
        buildConfigField("String", "CLOUDINARY_URL", "\"${properties.getProperty("CLOUDINARY_URL")}\"")
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
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
    implementation("com.google.android.gms:play-services-cast-tv:21.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.picasso:picasso:2.8")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //youtubeplayer
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0")

    //gif drawable
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.19")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.razorpay:checkout:1.6.33")

    //otp_view
//    implementation("io.github.chaosleung:pinview:1.4.4")

    implementation("com.google.firebase:firebase-messaging:23.4.0")


    implementation("com.auth0.android:jwtdecode:2.0.2")

    implementation ("com.cloudinary:cloudinary-core:1.35.0")

    implementation("com.cloudinary:cloudinary-android:2.5.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

   //QR generator
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    // QR Scanner
    implementation ("com.github.yuriy-budiyev:code-scanner:2.3.0")
}