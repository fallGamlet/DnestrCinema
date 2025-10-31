plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

kotlin {
    jvmToolchain(11)
}

android {
    namespace = "com.fallgamlet.dnestrcinema"
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    configurations.configureEach {
        resolutionStrategy.force("com.google.code.findbugs:jsr305:3.0.2")
    }

    defaultConfig {
        applicationId = "com.fallgamlet.dnestrcinema"
        minSdk = 23
        targetSdk = 36
        versionCode = 36
        versionName = "1.0.$versionCode"
        project.ext.set("archivesBaseName", "DnestrCinema$versionName")
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    productFlavors {}
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        stabilityConfigurationFiles = listOf(
            rootProject.layout.projectDirectory.file("stability_config.conf")
        )
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":KinotirApi"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.cons.core)
    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    // UI Tests
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.common.java8)
    runtimeOnly (libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.dagger)
    kapt (libs.dagger.compiler)
    kapt (libs.dagger.android.processor)

    implementation(libs.gson)

    implementation(libs.threetenabp)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.crashlytics.ktx)

    debugImplementation(libs.leakcanary.support.fragment)
    debugImplementation(libs.leakcanary.android)
    releaseImplementation(libs.leakcanary.android.no.op)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
