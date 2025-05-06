plugins {
    alias(libs.plugins.android.library)
}

android {
    //namespace = "com.tal_i.viewmodel"
    namespace = "com.dana_f.tashtit"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-tasks:18.0.2")

    implementation("com.squareup.okhttp3:okhttp:3.2.0")
    implementation("com.google.code.gson:gson:2.13.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.tasks)
    implementation(libs.firebase.firestore)
    implementation(project(":MODEL"))
    implementation(project(":REPOSITORY"))
    implementation(project(":HELPER"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}