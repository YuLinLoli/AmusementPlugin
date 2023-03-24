import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.7.22"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.14.0"
}

group = "com.mirai"
version = "1.0.0"

repositories {
//    mavenLocal()
//    maven("https://maven.aliyun.com/repository/gradle-plugin")
//    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}
dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.jsoup:jsoup:1.15.4")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
//    implementation("net.mamoe:mirai-core-jvm:2.14.0")

    testImplementation("net.mamoe:mirai-core-mock")
    compileOnly("net.mamoe:mirai-core")
    implementation("com.madgag:animated-gif-lib:1.4")
    implementation(platform("net.mamoe:mirai-bom:2.14.0"))

    implementation(kotlin("stdlib-jdk8"))
    //     compileOnly
    compileOnly("net.mamoe:mirai-core-utils")
    compileOnly("net.mamoe:mirai-console-compiler-common")
    compileOnly("org.bytedeco:javacv-platform:1.5.7")
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

