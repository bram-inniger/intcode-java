plugins {
    id("java")
    application
}

group = "be.inniger"
version = "1.0-SNAPSHOT"

application {
    mainClass = "be.inniger.intcode.problems.Day13\$Main"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaExec> { standardInput = System.`in` }
