# Books Viewer App

## Overview
Books Viewer is an Android application that allows users to search for books, view book details, mark books as favorites, and experience a user-friendly interface with support for dark mode.

---

## Features

- **Book Search**: Search for books using keywords.
- **Book Details Screen**: View detailed information about each book.
- **Favorites Screen**: Save and view your favorite books.
- **Dark Mode**: Seamless dark mode for comfortable night-time browsing.

---

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox (or later).
- Java Development Kit (JDK) version 8 or above.

### Steps to Run the Project

1. Clone the repository:
   ```bashhttps://github.com/Harshulsingla/android-assesment.git
   ```
2. Open the project in Android Studio.
3. Build and sync the Gradle files.
4. Run the application on an emulator or a physical device.

---

## Libraries Used

- **AndroidX Libraries**:
    - AppCompat: `androidx.appcompat:appcompat:1.7.0`
    - RecyclerView: `androidx.recyclerview:recyclerview:1.3.2`
    - Room: `androidx.room:room-common:2.6.1`, `androidx.room:room-runtime:2.6.1`
    - ConstraintLayout: `androidx.constraintlayout:constraintlayout:2.2.0`
    - CardView: `androidx.cardview:cardview:1.0.0`
    - Browser: `androidx.browser:browser:1.8.0`

- **Networking**:
    - OkHttp: `com.squareup.okhttp3:okhttp:4.10.0`
    - Retrofit: `com.squareup.retrofit2:retrofit:2.9.0`
    - Retrofit Gson Converter: `com.squareup.retrofit2:converter-gson:2.9.0`

- **Dependency Injection**:
    - Hilt: `com.google.dagger:hilt-android:2.51.1`

- **UI Components**:
    - Material Design: `com.google.android.material:material:1.12.0`
    - Glide for Image Loading: `com.github.bumptech.glide:glide:4.14.2`
---

## Known Limitations

- The app supports Android 9 (API level 28) and above.
- No offline caching for book search results.
- Limited to the APIs supported by the used services.

---

## Screenshots

### Book Search
[Insert Screenshot or Screen Recording Here]

### Book Details Screen
[Insert Screenshot or Screen Recording Here]

### Favorites Screen
[Insert Screenshot or Screen Recording Here]

### Dark Mode
[Insert Screenshot or Screen Recording Here]

---

## Build Configuration

### Gradle Build File
```gradle
plugins {
    id 'com.android.application'
    id 'com.google.dagger.hilt.android'
}

android {
    namespace 'com.example.booksviewer'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.booksviewer"
        minSdk 28
        targetSdk 34
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding true
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'com.google.android.material:material:1.12.0'
    implementation 'com.squareup.okhttp3:okhttp:4.10.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.10.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'androidx.room:room-common:2.6.1'
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.google.dagger:hilt-android:2.51.1'
    implementation 'androidx.browser:browser:1.8.0'
    annotationProcessor 'com.google.dagger:hilt-android-compiler:2.51.1'
    annotationProcessor 'androidx.hilt:hilt-compiler:1.0.0'
    annotationProcessor 'androidx.room:room-compiler:2.6.1'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'com.github.bumptech.glide:glide:4.14.2'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.14.2'
    implementation 'androidx.constraintlayout:constraintlayout:2.2.0'
    implementation 'androidx.cardview:cardview:1.0.0'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.2.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.6.1'
}
```

---

