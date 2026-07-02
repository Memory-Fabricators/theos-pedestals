pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net")
		maven("https://maven.kikugie.dev/snapshots")
		maven("https://maven.wagyourtail.xyz/snapshots")
		maven("https://mcentral.firstdark.dev/releases")
		mavenCentral()
		gradlePluginPortal()
	}
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.6"
}

rootProject.name = "theos-pedestals"

stonecutter {
    create(rootProject) { 
        versions("26.2")
    }
}
