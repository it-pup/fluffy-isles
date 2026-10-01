package pet.itpuppy.fluffyisles

import net.fabricmc.api.ModInitializer
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object FluffyIsles : ModInitializer {
	const val MOD_ID: String = "fluffy-isles"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	val client: Minecraft
		get() = Minecraft.getInstance()

	override fun onInitialize() {
		LOGGER.info("Woof!")
	}

	 fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
