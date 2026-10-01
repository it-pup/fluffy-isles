package pet.itpuppy.fluffyisles.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import pet.itpuppy.fluffyisles.FluffyIsles
import pet.itpuppy.fluffyisles.config.section.MiscSection
import pet.itpuppy.fluffyisles.config.section.RarityHighlightSection

@Config(name = FluffyIsles.MOD_ID)
class ModConfig : ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    var rarityHighlightSection = RarityHighlightSection()

    @ConfigEntry.Gui.CollapsibleObject
    var miscSection = MiscSection()
}