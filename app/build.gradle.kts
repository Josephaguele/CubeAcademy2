plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("dagger.hilt.android.plugin")
	id("kotlin-kapt")
	id("kotlin-parcelize")
}

android {
	namespace = "com.cube.cubeacademy"
	compileSdk = 34

	testOptions.unitTests.isReturnDefaultValues = true

	defaultConfig {
		applicationId = "com.cube.cubeacademy"
		minSdk = 30
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		buildConfigField("String", "API_URL", "\"https://cube-academy-api.cubeapis.com/\"")
		buildConfigField("String", "AUTH_TOKEN", "\"${project.property("authToken")}\"")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = "17"
	}

	buildFeatures {
		viewBinding = true
		buildConfig = true
	}

}

dependencies {

	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
	implementation("com.google.android.material:material:1.10.0")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.8")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.8")
	implementation ("androidx.activity:activity-ktx:1.8.0")

	// Hilt
	implementation("com.google.dagger:hilt-android:2.48")
	implementation("androidx.test.ext:junit-ktx:1.1.5")
	kapt("com.google.dagger:hilt-compiler:2.48")
	testImplementation("com.google.dagger:hilt-android-testing:2.48")
	androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
	kaptTest("com.google.dagger:hilt-android-compiler:2.48")
	kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")

	//test dependencies
	testImplementation("junit:junit:4.13.2")

	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	testImplementation("org.mockito:mockito-core:4.6.1")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
	androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
	testImplementation("io.mockk:mockk:1.12.4")
	androidTestImplementation("io.mockk:mockk:1.12.4")
	testImplementation("androidx.arch.core:core-testing:2.1.0")
	testImplementation ("androidx.test:runner:1.5.2")
	androidTestUtil ("androidx.test:orchestrator:1.1.0")
	androidTestImplementation("androidx.test:rules:1.1.2-alpha02")
	testImplementation ("org.robolectric:robolectric:4.7.3")




}