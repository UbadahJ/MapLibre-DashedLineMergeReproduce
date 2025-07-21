plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ubadahj.mergedlinedashreproduce"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ubadahj.mergedlinedashreproduce"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xopt-in=androidx.compose.ui.util.ExperimentalComposeUiApi",
                "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3ExpressiveApi"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    flavorDimensions += "renderer"
    productFlavors {
        create("opengl") {
            dimension = "renderer"

            resValue("string", "renderer", "OpenGL")
        }

        create("vulkan") {
            dimension = "renderer"

            resValue("string", "renderer", "Vulkan")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.core)
    "openglImplementation"(libs.maplibre.sdk.opengl)
    "vulkanImplementation"(libs.maplibre.sdk.vulkan)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}