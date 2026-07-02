plugins {
	java
	id("xyz.wagyourtail.unimined") version "1.4.2-SNAPSHOT"
}

repositories {
	mavenCentral()
    unimined.neoForgedMaven()
    unimined.wagYourMaven("snapshots")
}


dependencies {

}

unimined.minecraft {
	version = property("mcVersion").toString()

	neoForge {
		loader(property("forgeVersion").toString())
		// mixinConfig("mixins.example_mod.json")
	}
}

stonecutter {
	replacements.string {
		direction = eval(current.version, ">1.7.10")
		from = "cpw.mods.fml"
		to = "net.minecraftforge.fml"
	}
}
