plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    id(
        libs.plugins.hilt.plugin
            .get()
            .pluginId,
    )
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
                "proguard-rules.pro",
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
        compose = true
        dataBinding = true
        buildConfig = true
        viewBinding = true
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

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

dependencies {
    // modules
    implementation(project(":ui"))
    implementation(project(":domain"))
    implementation(project(":model"))

    // di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // coroutines
    implementation(libs.coroutines)

    // bundler
    implementation(libs.bundler)

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

    // logging
    implementation(libs.timber)

    // modules for unit test
    testImplementation(project(":network"))
    testImplementation(project(":database"))
}
