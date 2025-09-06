import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.terry1921.nenektrivia.network"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        val apiKeyProvider =
            providers
                .gradleProperty("CONFIG_API")
                .orElse(providers.environmentVariable("CONFIG_API"))
                .orElse(
                    providers.provider {
                        // Fallback a local.properties si existe (entorno local)
                        val lp = rootProject.file("local.properties")
                        if (lp.exists()) {
                            Properties()
                                .apply { lp.inputStream().use { load(it) } }
                                .getProperty("CONFIG_API", "")
                        } else {
                            ""
                        }
                    }
                )
        debug {
            isMinifyEnabled = false
            val key = apiKeyProvider.orNull ?: ""
            buildConfigField("String", "API_KEY", "\"$key\"")
        }
        release {
            isMinifyEnabled = true
            val key = apiKeyProvider.orNull ?: ""
            buildConfigField("String", "API_KEY", "\"$key\"")
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
    kotlin { jvmToolchain(17) }
    buildFeatures { buildConfig = true }
}

dependencies {
    implementation(project(":model"))

    // coroutines
    implementation(libs.coroutines)

    // network
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.sandwich)
    implementation(libs.gson)

    // di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // logging
    implementation(libs.timber)

    // test
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.okhttp.mockserver)
    testImplementation(libs.androidx.arch.core)
}
