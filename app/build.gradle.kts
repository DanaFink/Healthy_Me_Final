import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.dana_f.tashtit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dana_f.tashtit"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "RAPID_API_KEY",
            "\"${localProperties.getProperty("RAPID_API_KEY")}\""
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.picasso:picasso:2.8")





    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.recyclerview:recyclerview:1.2.1")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:3.2.0")
    implementation("com.google.code.gson:gson:2.13.0")

    implementation ("com.github.lzyzsd:circleprogress:1.2.1")


//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation(project(":VIEWMODEL"))
    implementation(project(":MODEL"))
    implementation(project(":HELPER"))
    implementation(project(":REPOSITORY"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}