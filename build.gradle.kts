plugins {
    java
    `maven-publish`
    `java-library`
}

group = "io.github.thegatesdev"
version = "4.1.0"
description = "A clean, type safe configuration structure."

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks {
    test {
        useJUnitPlatform()
    }
}


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