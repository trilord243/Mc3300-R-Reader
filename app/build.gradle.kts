plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "live.trilord.rfid_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "live.trilord.rfid_app"
        minSdk = 30
        targetSdk = 34
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
    }
    buildFeatures {
        compose = true
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
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_ASCII-release-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_CMN-release-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_INTERFACE-release-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_LLRP-release-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_NGE-protocolrelease-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_NGE-Transportrelease-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_NGEUSB-Transportrelease-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_READER-release-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\API3_TRANSPORT-release-2.0.4.177.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\BarcodeScannerLibrary-release-6.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\rfidhostlib.aar"))
    implementation(files("E:\\devices\\RFID-api\\Zebra_RFIDAPI3_SDK_2.0.4.177\\rfidseriallib.aar"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}