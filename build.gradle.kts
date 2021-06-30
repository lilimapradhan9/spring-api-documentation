import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    application
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

apply {
    plugin("org.asciidoctor.jvm.convert")
}

group = "com.lilima.github"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient:2.0.3.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.0")
    testImplementation("io.projectreactor:reactor-test:3.2.6.RELEASE")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0-M1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.0-M1")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.10")
    testImplementation("io.mockk:mockk:1.8.9.kotlin13")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "SpringApiDocumentationKt"
}

val snippetsDir = file("build/generated-snippets")

configurations {
    create("asciidoctor")
}

tasks {
    test {
        useJUnitPlatform()
        outputs.dir(file("build/generated-snippets"))
    }

    asciidoctor {
        mustRunAfter("test")
        setSourceDir(snippetsDir)
        setOutputDir(file("src/output/"))
        baseDirFollowsSourceDir()
        options = mapOf(
            "doctype" to "book",
            "backend" to "html5"
        )
        attributes = (
            mapOf(
                "snippets" to file("$buildDir/generated-snippets"),
                "source-highlighter" to "coderay",
                "toclevels" to "3",
                "sectlinks" to "true",
                "data-uri" to "true",
                "nofooter" to "true"
            )
        )

    }
    asciidoctorj {
        fatalWarnings("include file not found")
        modules { diagram.use() }
    }
    build {
        dependsOn("asciidoctor")
    }

}

tasks.build { dependsOn(tasks.asciidoctor) }
