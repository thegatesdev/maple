plugins {
    java
    `maven-publish`
    `java-library`
}

group = "io.github.thegatesdev"
version = "4.0.0"
description = "A clean, type safe configuration structure."


java {
    withJavadocJar()
    withSourcesJar()

    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}