plugins {
    java
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    jacoco
}

group = "com.vr.mini.autorizador"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    doFirst {
        if (!tasks.test.get().didWork) {
            throw GradleException("No tests were executed. Ensure you have test cases in your project.")
        }
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

tasks.build {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

tasks.build {
    dependsOn(tasks.check)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    doFirst {
        if (testClassesDirs.files.isEmpty()) {
            throw GradleException("No test classes found. Ensure you have test cases in your project.")
        }
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["validationApiVersion"] = "2.0.1.Final"
extra["mapstructVersion"] = "1.5.3.Final"
extra["lombokVersion"] = "1.18.28"
extra["springdocVersion"] = "2.1.0"
extra["gsonVersion"] = "2.8.8"
extra["jacocoAgentVersion"] = "0.8.8"
extra["commonsLang3Version"] = "3.12.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
    implementation("javax.validation:validation-api:${property("validationApiVersion")}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("springdocVersion")}")
    implementation("org.apache.commons:commons-lang3:${property("commonsLang3Version")}")
    implementation("com.google.code.gson:gson:${property("gsonVersion")}")
    jacocoAgent("org.jacoco:org.jacoco.agent:${property("jacocoAgentVersion")}")

    // Lombok e MapStruct juntos
    compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.withType<Test> {
    useJUnitPlatform()
}


