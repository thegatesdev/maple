plugins {
    java
    `maven-publish`
}

group = "com.github.thegatesdev"
version = "5.0.0-SNAPSHOT"

java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing.publications.create<MavenPublication>("mavenJava").from(components["java"])