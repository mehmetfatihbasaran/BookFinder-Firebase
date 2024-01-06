buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.20")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.20-1.0.14")
        classpath("com.google.gms:google-services:4.4.0")
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }
    configurations.all {
        resolutionStrategy {
            force("org.xerial:sqlite-jdbc:3.36.0")
        }
    }
}