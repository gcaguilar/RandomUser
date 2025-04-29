plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.gcaguilar.randomuser"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    flavorDimensions += "default"
    productFlavors {
        create("mock") { dimension = "default" }
        create("production") { dimension = "default" }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.ktor)
    implementation(libs.koin.core)
    implementation(libs.ktor.mock.client)
}