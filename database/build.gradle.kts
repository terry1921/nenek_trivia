import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "dev.terry1921.nenektrivia.database"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        val dbNameProvider =
            providers
                .gradleProperty("DATABASE_NAME")
                .orElse(providers.environmentVariable("DATABASE_NAME"))
                .orElse(
                    providers.provider {
                        val lp = rootProject.file("local.properties")
                        if (lp.exists()) {
                            Properties()
                                .apply { lp.inputStream().use { load(it) } }
                                .getProperty("DATABASE_NAME", "default.db")
                        } else {
                            "default.db"
                        }
                    }
                )
        debug {
            isMinifyEnabled = false
            val dbName = dbNameProvider.orNull ?: "default.db"
            buildConfigField("String", "DATABASE_NAME", "\"$dbName\"")
        }
        release {
            isMinifyEnabled = true
            val dbName = dbNameProvider.orNull ?: "default.db"
            buildConfigField("String", "DATABASE_NAME", "\"$dbName\"")
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
    sourceSets.named("test") { assets.srcDirs(files("$projectDir/schemas")) }
    testOptions { unitTests.isIncludeAndroidResources = true }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.generateKotlin", "true")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

dependencies {
    implementation(project(":model"))

    // coroutines
    implementation(libs.coroutines)

    // datastore
    implementation(libs.datastore)

    // database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // json parsing
    implementation(libs.gson)

    // di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // unit test
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.arch.core)
}
