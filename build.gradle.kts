plugins {
    java
    `maven-publish`
    `java-library`
}

group = "io.github.thegatesdev"
version = "4.0.0"
description = "A clean, type safe configuration structure."

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}