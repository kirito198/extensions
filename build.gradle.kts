import com.android.build.gradle.BaseExtension
import com.lagradost.cloudstream3.gradle.CloudstreamExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.3")
        classpath("com.github.recloudstream:gradle:-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

project(":KrmiziProvider") {
    apply(plugin = "com.android.library")
    apply(plugin = "kotlin-android")
    apply(plugin = "com.lagradost.cloudstream3.gradle")

    extensions.getByName<CloudstreamExtension>("cloudstream").apply {
        setRepo("https://github.com/kirito198/extensions")
    }

    extensions.getByName<BaseExtension>("android").apply {
        namespace = "com.kirito198"
        defaultConfig {
            minSdk = 21
            compileSdkVersion(35)
            targetSdk = 35
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        tasks.withType<KotlinJvmCompile> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
    }

    dependencies {
        val implementation by configurations
        implementation("com.github.recloudstream.cloudstream:library:-SNAPSHOT")
        implementation(kotlin("stdlib"))
        implementation("com.github.Blatzar:NiceHttp:0.4.11")
        implementation("org.jsoup:jsoup:1.18.3")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    }
}

task<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
