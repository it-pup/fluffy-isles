package pet.itpuppy.fluffyisles

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderLookup
import net.minecraft.data.registries.VanillaRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pet.itpuppy.fluffyisles.commands.ConfigCommand
import pet.itpuppy.fluffyisles.config.ModConfig

object FluffyIsles : ModInitializer {
	const val MOD_ID: String = "fluffy-isles"
	private val lookup by lazy { VanillaRegistries.createLookup() }
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	lateinit var config: ModConfig

	val registry: HolderLookup.Provider get() = client.connection?.registryAccess() ?: lookup

	val client: Minecraft
		get() = Minecraft.getInstance()

	override fun onInitialize() {
		LOGGER.info("Woof!")

		AutoConfig.register(ModConfig::class.java, ::GsonConfigSerializer)
		config = AutoConfig.getConfigHolder(ModConfig::class.java).config

		ConfigCommand.register()
	}

	fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)

	fun message(component: Component) {
		client.gui.chat.addMessage(component)
	}

	fun clipboard(data: String) {
		client.keyboardHandler.clipboard = data
	}
}
