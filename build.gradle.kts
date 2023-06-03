plugins {
    `java-library`
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
version = "2.0.0"
description = "A clean type safe data structure."
java.sourceCompatibility = JavaVersion.VERSION_17

java {
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}