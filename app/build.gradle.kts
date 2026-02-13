plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
    id(libs.plugins.hilt.plugin.get().pluginId)
}

android {
    namespace = "dev.terry1921.nenektrivia"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.terry1921.nenektrivia"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    flavorDimensions += "store"
    productFlavors {
        create("playstore") {
            dimension = "store"
            buildConfigField("Boolean", "FIREBASE_TEST", "false")
        }
        create("firebase") {
            dimension = "store"
            versionNameSuffix = ".debug"
            buildConfigField("Boolean", "FIREBASE_TEST", "true")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        buildConfig = true
    }
    hilt {
        enableAggregatingTask = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    // modules
    implementation(project(":ui"))

    // androidx
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.splashscreen)

    // di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // coroutines
    implementation(libs.coroutines)

    // bundler
    implementation(libs.bundler)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // whatIf
    implementation(libs.whatif)

    // unit test
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.coroutines.test)

    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.android.test.runner)

    // Hilt en androidTest con KSP
    androidTestImplementation(libs.hilt.testing)
    kspAndroidTest(libs.hilt.compiler)

    // modules for unit test
    testImplementation(project(":network"))
    testImplementation(project(":database"))
}
