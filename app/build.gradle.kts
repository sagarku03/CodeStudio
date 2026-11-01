import groovy.json.JsonSlurper

val versionFile = rootProject.file("version.json")
val parsedVersion = if (versionFile.exists()) {
    @Suppress("UNCHECKED_CAST")
    JsonSlurper().parseText(versionFile.readText()) as Map<String, Any>
} else {
    mapOf("version_code" to 1, "version_name" to "1.0.0")
}
val appVersionCode = (parsedVersion["version_code"] as? Number)?.toInt() ?: 1
val appVersionName = (parsedVersion["version_name"] as? String) ?: "1.0.0"

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.wire)
  alias(libs.plugins.secrets)
}

android {
    namespace = "com.example.gemini"
    compileSdk { version = release(36) { minorApiLevel = 1 } }

    defaultConfig {
        applicationId = "com.codestudio.ide"
        minSdk = 24
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName

        buildConfigField("int", "BRIDGE_PORT", "1236")
        buildConfigField("int", "HUB_PORT", "1237")
        buildConfigField("int", "BROWSER_MCP_PORT", "8767")
        val geminiKey = System.getenv("GEMINI_API_KEY") ?: ""
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiKey\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
            storeFile = file(keystorePath)
            storePassword = System.getenv("STORE_PASSWORD")
            keyAlias = "upload"
            keyPassword = System.getenv("KEY_PASSWORD")
        }
        create("debugConfig") {
            storeFile = file("${rootDir}/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            isCrunchPngs = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            signingConfig = signingConfigs.getByName("debugConfig")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
        disable += "ExpiredTargetSdkVersion"
    }

    buildFeatures {
      compose = true
      aidl = false
      buildConfig = true
      shaders = false
    }

    androidResources {
        noCompress += listOf("gz", "tgz", "tar.gz", "tar")
    }

    packaging {
      resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
        excludes += "*.xsd"
        excludes += "**/*.xsd"
        excludes += "META-INF/INDEX.LIST"
        excludes += "META-INF/io.netty.versions.properties"
        excludes += "META-INF/DEPENDENCIES"
        excludes += "META-INF/LICENSE*"
        excludes += "META-INF/NOTICE*"
      }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = true
    }
}

secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
  ignoreList.add("GEMINI_API_KEY")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)

  // Core Android dependencies
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
  implementation("androidx.work:work-runtime-ktx:2.10.0")

  // Arch Components
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  // Compose
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.material.icons.extended)

  // Tooling
  debugImplementation(libs.androidx.compose.ui.tooling)

  // Networking and Serialization
  implementation(libs.okhttp)
  implementation(libs.okhttp.sse)
  implementation(libs.androidx.webkit)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.androidx.datastore.preferences)
  implementation("com.telemetrydeck:kotlin-sdk:6.0.1")

  // Image Loading (Coil for Markdown Images and IDE Asset Viewer)
  implementation("io.coil-kt:coil-compose:2.7.0")
  implementation("io.coil-kt:coil-svg:2.7.0")

  // Native Android JLaTeXMath / LaTeX Renderer (Pure Android Canvas, Zero WebViews)
  implementation("io.noties.markwon:ext-latex:4.6.2")
  implementation("ru.noties:jlatexmath-android:0.2.0")

  // Pure Java/Kotlin SSH Client (Persistent Termux SSH connection pool)
  implementation("com.github.mwiede:jsch:0.2.20")

  // HTML Parser & Web Content Scraper (Free Web Search & Webpage Reader)
  implementation("org.jsoup:jsoup:1.18.3")

  // Sora Editor (Industry standard Android Code Editor)
  implementation("io.github.Rosemoe.sora-editor:editor:0.23.6")
  implementation("io.github.Rosemoe.sora-editor:language-textmate:0.23.6")
  implementation("io.github.Rosemoe.sora-editor:language-java:0.23.6")

  // Archive and Compression Utilities (Supports tar, xz, ar, deb extraction)
  implementation("org.apache.commons:commons-compress:1.26.1")
  implementation("org.tukaani:xz:1.9")

  // Termux Terminal Emulator with Native PTY Support (Terminal View is compiled from source)
  implementation("com.github.termux.termux-app:terminal-emulator:v0.118.1")
  implementation("com.google.guava:listenablefuture:1.0")

  // Wire Protobuf & gRPC runtime
  implementation(libs.wire.runtime)
  implementation(libs.wire.grpc.client)

  // Navigation
  implementation(libs.androidx.navigation3.ui)
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}

configurations.all {
  resolutionStrategy {
    force("com.squareup.okhttp3:okhttp:4.12.0")
    force("com.squareup.okhttp3:okhttp-android:4.12.0")
  }
}

wire {
  sourcePath {
    srcDir("src/main/proto")
  }
  kotlin {
    rpcRole = "client"
    rpcCallStyle = "suspending"
  }
}
