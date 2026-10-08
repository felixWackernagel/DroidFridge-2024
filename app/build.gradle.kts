plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.android.room)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.google.oss.licenses)
}

android {
    namespace = "de.wackernagel.droidfridge"
    compileSdk = 37

    defaultConfig {
        applicationId = "de.wackernagel.droidfridge"
        minSdk = 29
        targetSdk = 37
        versionCode = 5
        versionName = "1.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        dataBinding = false
        buildConfig = true
    }

    sourceSets {
        getByName("androidTest") {
            assets.directories.add("$projectDir/schemas")
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        freeCompilerArgs.add("-Xjdk-release=17")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // Hilt DI
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.fragment)
    implementation(libs.androidx.hilt.work)

    // Room
    implementation(libs.android.room.runtime)
    ksp(libs.android.room.compiler)

    // AndroidX Lifecycle / ViewModel / LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Coroutine
    implementation(libs.android.coroutines)

    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.worker.runtime.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.coroutines.test)
    androidTestImplementation(libs.androidx.worker.testing)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.android.room.testing)
    androidTestImplementation(libs.kotlinx.serialization.json)

    // OSS Licenses
    implementation(libs.play.services.oss.licenses)

    // DataStore
    implementation(libs.datastore.preferences)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Image Loading
    implementation(libs.coil)
}