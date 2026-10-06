package pet.itpuppy.fluffyisles.collectors

import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.PlayerScoreEntry
import net.minecraft.world.scores.PlayerTeam
import pet.itpuppy.fluffyisles.FluffyIsles

object ScoreboardCollector {
    private var cached: List<String> = listOf()

    private fun collect(): List<String> {
        val level = FluffyIsles.client.level ?: return listOf()

        val scoreboard = level.scoreboard
        val objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR) ?: return listOf()

        val lines = scoreboard.listPlayerScores(objective)
            .filterNot { it.isHidden }
            .sortedWith(
                compareByDescending<PlayerScoreEntry> { it.value }
                    .thenBy(String.CASE_INSENSITIVE_ORDER) { it.owner }
            )
            .take(15)
            .mapTo(mutableListOf()) {
                val team = scoreboard.getPlayersTeam(it.owner)
                PlayerTeam.formatNameForTeam(team, it.ownerName()).string
            }

        lines.addFirst(objective.displayName.string)
        return lines
    }

    fun tick() {
        cached = collect()
    }

    fun getOrNull(index: Int): String? {
        return cached.getOrNull(index)
    }

    fun getFull(): List<String> {
        return cached
    }
}