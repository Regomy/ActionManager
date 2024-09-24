plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.freefair.lombok") version "6.6"
}

group = "me.rejomy"
version = "1.0"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    flatDir {
        dirs("libs")  // Указываем папку libs как источник зависимостей
    }
}

dependencies {
    compileOnly("me.clip:placeholderapi:2.11.5")

    compileOnly(files("libs/server.jar"))  // Указываем путь к JAR-файлу напрямую

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("org.reflections:reflections:0.10.2")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    relocate("org.reflections.Reflections", "me.rejomy.actions.shaded.reflections")
}