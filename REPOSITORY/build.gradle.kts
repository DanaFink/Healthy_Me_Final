import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.gms.google.services)
}
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}
android {
    namespace = "com.dana_f.tashtit"
    //namespace = "com.tal_i.repository"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")


        buildConfigField(
            "String",
            "REPOSITORY_API_KEY",
            "\"${localProperties.getProperty("REPOSITORY_API_KEY")}\""
        )
    }
    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:3.2.0")
    implementation("com.google.code.gson:gson:2.13.0")

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1") // for Java


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(project(":MODEL"))
    implementation(project(":HELPER"))
    implementation(libs.picasso)
    implementation(libs.mpandroidchart)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}