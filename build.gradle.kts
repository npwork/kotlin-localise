import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.npwork"
version = "1.0.0-SNAPSHOT"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.41"

    id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.4.0"
    id("info.solidsoft.pitest") version "1.4.6"
    id("com.star-zero.gradle.githook") version "1.2.0"

    jacoco
    maven
}

val mainClass: String by project

val tornadoFxVersion = "1.7.19"
val junitVersion = "5.5.2"

repositories {
    jcenter()
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven(url = "https://plugins.gradle.org/m2")
}

dependencies {
    // Kotlin
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.github.wnameless:json-flattener:0.2.2")
    implementation("khttp:khttp:1.0.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.github.mmazi:rescu:1.6.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.+")
    implementation("com.google.guava:guava:20.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.assertj:assertj-core:3.14.0")
}

detekt {
    failFast = true
    buildUponDefaultConfig = true
    config = files("gradle/detekt/detekt.yml")
    reports {
        html.enabled = true
        xml.enabled = false
        txt.enabled = false
    }
}

tasks.test {
    useJUnitPlatform()
    failFast = true
    testLogging {
        events("passed", "skipped", "failed")
    }

    configure<JacocoTaskExtension> {
        isEnabled = true
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.allWarningsAsErrors = true
}

tasks.wrapper {
    gradleVersion = "6.1"
}

jacoco {
    toolVersion = "0.8.5"
}

pitest {
    targetClasses.add("com.cardiolyse.holter.*")
    outputFormats.add("HTML")
}

githook {
    createHooksDirIfNotExist = true
    hooks {
        create("pre-commit") {
            task = "build -x test"
            shell = "echo 'Build successful'"
        }
    }
}

tasks.jacocoTestReport {
    executionData.setFrom(fileTree(buildDir).include("/jacoco/*.exec"))

    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.isEnabled = true
    }
}
