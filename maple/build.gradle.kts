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

repositories{
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks{
    test{
        useJUnitPlatform()
    }
}

publishing.publications.create<MavenPublication>("mavenJava").from(components["java"])
