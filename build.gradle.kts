import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "6.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("org.ajoberstar:gradle-git:1.2.0")
    }
}

apply {
    plugin("java")
}

//RECODE.RELEASE.PATCH.DEVELOPMENT
version = "1.0.7-SNAPSHOT"
group = "com.diamonddagger590"

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") //Papi
    maven("https://nexus.wesjd.net/repository/thirdparty/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")

    //Spigot
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/central/")
    maven("https://repo.md-5.net/content/repositories/snapshots/")
    maven("https://repo.md-5.net/content/repositories/releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")

}

dependencies {

    val mccoreVersion = "1.0.0.6-SNAPSHOT"
    implementation("com.diamonddagger590:McCore:$mccoreVersion")

    val bstatsVersion = "2.2.1"
    implementation("org.bstats:bstats-bukkit:$bstatsVersion")

    val placeholderAPIVersion = "2.9.2"
    compileOnly("me.clip:placeholderapi:$placeholderAPIVersion")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ProcessResources> {
    filesMatching("**/*.yml") {
        expand(project.properties)
    }
}

//tasks.jar {
//
//    manifest.attributes["Manifest-Version"] = "1.0"
//    manifest.attributes["Main-Class"] = "com.diamonddagger590.communitygoals.CommunityGoals"
//    manifest.attributes["Class-Path"] = "CommunityGoals/libs/h2.jar"
//
//    // Open git
//    val git = org.ajoberstar.grgit.Grgit.open(file("."))
//    // Use abbreviated id from git head
//    archiveAppendix.set(git.head().abbreviatedId)
//
//    // check if classifier is present before adding an unnecessary '-'.
//    val classifier = archiveClassifier.get()
//
//    // Set our desired formatting for the file name
//    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-${archiveAppendix.get()}${if (classifier.isEmpty()) "" else "-$classifier"}.${archiveExtension.get()}")
//}

tasks {
    shadowJar {

        //My gheto solution to get the commit hash on the shadow'd jar

        // Open git
        val git = org.ajoberstar.grgit.Grgit.open(file("."))
        // Use abbreviated id from git head
        archiveAppendix.set(git.head().abbreviatedId)

        // check if classifier is present before adding an unnecessary '-'.
        val classifier = archiveClassifier.get()

        // Set our desired formatting for the file name
        archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-${archiveAppendix.get()}${if (classifier.isEmpty()) "" else "-$classifier"}.${archiveExtension.get()}")

        mergeServiceFiles()
        relocate("org.bstats", "com.diamonddagger590.communitygoals")
        relocate("ch.jalu.configme", "com.diamonddagger590.communitygoals.configme")
        relocate("com.diamonddagger590.mccore", "com.diamonddagger590.communitygoals.mccore")
    }
}

tasks {
    build {
        dependsOn(compileJava)
        dependsOn(shadowJar)
    }
    jar {
        dependsOn(shadowJar)
    }
}