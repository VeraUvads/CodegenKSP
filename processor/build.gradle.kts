plugins {
    id("java-library")
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.9.0-1.0.11"
}

//ksp {
//    arg("autoserviceKsp.verify", "true")
//    arg("autoserviceKsp.verbose", "true")
//}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
    implementation(project(":annotation"))
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.11")
    implementation("com.google.auto.service:auto-service-annotations:1.0")

    implementation("com.squareup:kotlinpoet:1.10.1")
    implementation("com.squareup:kotlinpoet-ksp:1.10.1")
//    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.11")
//    implementation("com.google.auto.service:auto-service-annotations:1.0")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")
}