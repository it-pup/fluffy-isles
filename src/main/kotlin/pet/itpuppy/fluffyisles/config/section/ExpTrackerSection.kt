package pet.itpuppy.fluffyisles.config.section

import me.shedaniel.autoconfig.annotation.ConfigEntry
import pet.itpuppy.fluffyisles.config.entries.SkillSelect

class ExpTrackerSection {
    @ConfigEntry.Gui.Tooltip
    var isEnabled: Boolean = false

    @ConfigEntry.Gui.Tooltip
    var idleTimeoutSeconds: Int = 60

    @ConfigEntry.Gui.Tooltip
    var hideAfterSeconds: Int = 300

    @SkillSelect
    var hiddenSkills: MutableList<String> = mutableListOf()
}