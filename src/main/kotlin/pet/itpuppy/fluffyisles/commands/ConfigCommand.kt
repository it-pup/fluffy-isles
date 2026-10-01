package pet.itpuppy.fluffyisles.commands

import me.shedaniel.autoconfig.AutoConfigClient
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import pet.itpuppy.fluffyisles.FluffyIsles
import pet.itpuppy.fluffyisles.config.ModConfig

object ConfigCommand {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal(FluffyIsles.MOD_ID).executes {
                    FluffyIsles.client.execute {
                        FluffyIsles.client.setScreenAndShow(
                            AutoConfigClient.getConfigScreen(ModConfig::class.java, FluffyIsles.client.screen).get()
                        )
                    }

                    1
                }
            )
        }
    }
}