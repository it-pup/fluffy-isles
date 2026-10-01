package pet.itpuppy.fluffyisles.integration

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfigClient
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import pet.itpuppy.fluffyisles.config.ModConfig

@Environment(EnvType.CLIENT)
class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            AutoConfigClient.getConfigScreen(ModConfig::class.java, parent).get()
        }
    }
}