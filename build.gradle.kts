import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("fabric-loom") version "1.10.5"
	id("maven-publish")
	id("org.jmailen.kotlinter") version "5.0.1"
	id("org.jetbrains.kotlin.jvm") version "2.1.20"
}

repositories {
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("pedestals") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.maybeCreate("client"))
		}
	}

}


tasks.processResources {
	inputs.property("version", project.version)
	filesMatching(listOf("fabric.mod.json")) {
		expand(
			"version" to "1.1.0",
		)
	}
}

dependencies {
	minecraft("com.mojang:minecraft:1.21.5")
	mappings("net.fabricmc:yarn:1.21.5+build.1:v2")
	modImplementation("net.fabricmc:fabric-loader:0.16.10")
	modImplementation("net.fabricmc.fabric-api:fabric-api:0.119.6+1.21.5")
	modImplementation("net.fabricmc:fabric-language-kotlin:1.13.2+kotlin.2.1.20")
}

//processResources {
//	inputs.property "version", project.version
//
//	filesMatching("fabric.mod.json") {
//		expand "version": inputs.properties.version
//	}
//}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
	options.encoding = "UTF-8"
	options.release = 21
}

kotlin {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_21)
	}
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}
