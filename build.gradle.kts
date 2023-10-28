plugins {
    java
    idea
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

group = "io.github.thegatesdev"
version = "4.0.0"
description = "A clean, type safe configuration structure."
java.sourceCompatibility = JavaVersion.VERSION_17

java {
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}