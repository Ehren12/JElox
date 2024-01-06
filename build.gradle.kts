plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation("org.mockito:mockito-core:3.+")
    testImplementation("org.assertj:assertj-core:3.25.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

application {
    mainClass = "com.ehren.jlox.Lox"
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "com.ehren.jlox.Lox"
    }
}
