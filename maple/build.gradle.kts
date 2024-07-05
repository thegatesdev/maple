plugins {
    java
    `maven-publish`
}


group = "io.github.thegatesdev"
version = "5.0.0-SNAPSHOT"
description = "A clean, type safe intermediary structure"


java {
    toolchain{
        languageVersion = JavaLanguageVersion.of(21)
    }

    withSourcesJar()
    withJavadocJar()
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

publishing{
    publications{
        create<MavenPublication>("maven"){
            from(components["java"])

            pom{
                name = "Maple"
                description = project.description
                url = "https://github.com/thegatesdev/maple"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "thegatesdev"
                        name = "Timar Karels"
                    }
                }
            }
        }
    }
}
