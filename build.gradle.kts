plugins {
    java
    `maven-publish`
}

group = "io.github.thegatesdev"
version = "4.0.0"
description = "A clean, type safe configuration structure."
java.sourceCompatibility = JavaVersion.VERSION_17

java {
    withSourcesJar()
}