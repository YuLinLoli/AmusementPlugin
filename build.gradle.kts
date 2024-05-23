import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.8.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.16.0"
    id("com.github.gmazzo.buildconfig") version "4.1.1"
}

group = "com.yulin"
version = "1.0.2"
buildConfig {
    className("BuildConfig")
    packageName("com.yulin.cg")
    buildConfigField("String", "yulinVersion", "\"${version}\"")
    buildConfigField("String", "name", "\"AmusementPlugin\"")
    buildConfigField("String", "id", "\"com.yulin.AmusementPlugin\"")
}
mirai {
    jvmTarget = JavaVersion.VERSION_11
    noTestCore = true
    setupConsoleTestRuntime {
        // 移除 mirai-core 依赖
        classpath = classpath.filter {
            !it.nameWithoutExtension.startsWith("mirai-core-jvm")
        }
    }
}
repositories {
    mavenLocal()
//    maven("https://maven.aliyun.com/repository/gradle-plugin")
//    maven("https://maven.aliyun.com/repository/central")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
}
dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.jsoup:jsoup:1.15.4")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("net.mamoe:mirai-core-jvm:2.16.0")
    testConsoleRuntime("top.mrxiaom:overflow-core:2.16.0-d59ef26-SNAPSHOT")

    implementation("com.madgag:animated-gif-lib:1.4")
    compileOnly("org.bytedeco:javacv-platform:1.5.7")
//    compileOnly
    implementation(kotlin("stdlib-jdk8"))
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

