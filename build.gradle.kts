import org.gradle.kotlin.dsl.mappings
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("net.fabricmc.fabric-loom-remap")
	id("org.jetbrains.kotlin.jvm") version "2.4.20"

	`maven-publish`
}

repositories {
	// Add repositories to retrieve artifacts from in here.
	maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
	maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
	maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
	maven("https://maven.shedaniel.me/")
}

dependencies {
	minecraft(libs.minecraft)

	mappings(loom.layered {
		officialMojangMappings()
		parchment(libs.parchment)
	})

	modImplementation(libs.bundles.fabric)
	modImplementation(libs.modmenu)
	modApi(libs.cloth.config)

	modRuntimeOnly(libs.devauth)
}

tasks.processResources {
	fun MutableMap<String, Any>.register(key: String, property: String) {
		val value = project.property(property).toString()
		inputs.property(key, value)
		set(key, value)
	}

	val props = buildMap {
		register("name", "name")
		register("description", "description")
		register("version", "version")
	}

	filesMatching("fabric.mod.json") {
		expand(props)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_21
	}
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories { }
}
