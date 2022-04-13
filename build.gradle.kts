import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("com.google.protobuf") version "0.8.18"

    kotlin("plugin.jpa") version "1.5.10"

    kotlin("plugin.allopen") version "1.5.10"
    kotlin("plugin.noarg") version "1.5.10"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("io.grpc:grpc-protobuf:1.43.2")
    implementation("io.grpc:grpc-stub:1.43.2")
    implementation("io.grpc:grpc-kotlin-stub:1.1.0")
    implementation("io.grpc:grpc-services:1.43.2")
    implementation("net.devh:grpc-spring-boot-starter:2.13.1.RELEASE")

    implementation("com.google.protobuf:protobuf-java:3.19.3")
    implementation("com.google.protobuf:protobuf-java-util:3.19.3")

    testImplementation("org.mockito:mockito-inline:2.13.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation(kotlin("test"))

    implementation("com.google.code.gson:gson:2.8.0")

    runtimeOnly("com.h2database:h2")
}

sourceSets {
    main {
        proto {
        }
        java {
            setSrcDirs(
                listOf(
                    "build/generated/source/proto/main/grpckt",
                    "build/generated/source/proto/main/grpc",
                    "build/generated/source/proto/main/java"
                )
            )
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.17.0"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.43.2"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.1.0:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}