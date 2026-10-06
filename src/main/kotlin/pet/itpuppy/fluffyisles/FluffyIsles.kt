package pet.itpuppy.fluffyisles

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderLookup
import net.minecraft.data.registries.VanillaRegistries
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pet.itpuppy.fluffyisles.commands.ClientCommand
import pet.itpuppy.fluffyisles.config.ModConfig
import pet.itpuppy.fluffyisles.events.ClientEvent
import pet.itpuppy.fluffyisles.utils.Comp
import pet.itpuppy.fluffyisles.utils.Comp.of
import pet.itpuppy.fluffyisles.utils.Comp.prefix

object FluffyIsles : ModInitializer {
	const val MOD_ID: String = "fluffy-isles"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	lateinit var config: ModConfig

	private val lookup by lazy { VanillaRegistries.createLookup() }

	val registry: HolderLookup.Provider get() = client.connection?.registryAccess() ?: lookup
	val client: Minecraft get() = Minecraft.getInstance()

	val prefix = Comp.build(
		of("[", ChatFormatting.GRAY),
		of("Fluffy Isles", ChatFormatting.LIGHT_PURPLE),
		of("] ", ChatFormatting.GRAY)
	)

	override fun onInitialize() {
		LOGGER.info("Woof!")

		AutoConfig.register(ModConfig::class.java, ::GsonConfigSerializer)
		config = AutoConfig.getConfigHolder(ModConfig::class.java).config

		ClientCommand::class.sealedSubclasses
			.mapNotNull { it.objectInstance }
			.forEach { it.register() }

		ClientEvent::class.sealedSubclasses
			.mapNotNull { it.objectInstance }
			.forEach { it.register() }
	}

	fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)

	fun message(component: MutableComponent) {
		client.gui.chat.addMessage(
			component.prefix(prefix)
		)
	}

	fun clipboard(data: String) {
		client.keyboardHandler.clipboard = data
	}
}
