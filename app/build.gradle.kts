plugins {


        alias(libs.plugins.android.application)
        alias(libs.plugins.ksp)
        alias(libs.plugins.hilt)
    }


android {
    namespace = "com.nweave.productviewer"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.nweave.productviewer"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.hilt.android)
    implementation(libs.shimmer)
    implementation(libs.picasso)
    implementation(libs.universal.image.loader)
    ksp(libs.hilt.compiler)

}