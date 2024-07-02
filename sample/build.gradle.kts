plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}
android {
    namespace = "cn.lalaki.demo10086"
    compileSdk = 35
    defaultConfig {
        applicationId = namespace
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "1.0"
        resourceConfigurations.add("en")
    }
    signingConfigs {
        register("release") {
            storeFile = file("D:\\imoe.jks")
            keyAlias = "dazen@189.cn"
            storePassword = System.getenv("mystorepass")
            keyPassword = System.getenv("mystorepass2")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
            isJniDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions.jvmTarget = "21"
    buildToolsVersion = "35.0.0"
}
tasks.configureEach {
    if (arrayOf("aarmetadata", "artprofile", "jni", "native").any {
            name.contains(it, ignoreCase = true)
        }
    ) {
        enabled = false
    }
}
dependencies {
    // implementation("cn.lalaki:IceBox-SDK.kt:1.1")
    implementation(project(":sdk"))
    implementation("androidx.recyclerview:recyclerview:1.4.0-alpha01")
}
