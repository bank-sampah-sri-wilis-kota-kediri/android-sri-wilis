plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.bs.sriwilis"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bs.sriwilis"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        val mainUrl: String by project
        buildConfigField("String", "mainUrl", "\"$mainUrl\"")

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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //implementation("androidx.activity:activity:1.9.0")
    //implementation("androidx.fragment:fragment-ktx:1.5.1")

    //datastore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.livedata.ktx.v231)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //ui
    implementation(libs.ucrop)
    implementation(libs.circleimageview)
    implementation(libs.material.v130alpha03)
    implementation(libs.glide)

    //room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)
    kapt(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.material)
    implementation(libs.androidx.swiperefreshlayout)

    /*  //ui
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
            implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
            implementation("androidx.activity:activity-ktx:1.9.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
            implementation("androidx.core:core-splashscreen:1.0.1")
            implementation("de.hdodenhof:circleimageview:3.1.0")
            implementation("com.github.Ferfalk:SimpleSearchView:0.2.1")
            //implementation(libs.ucrop)
            implementation("androidx.multidex:multidex:2.0.1")
            implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")*/
}