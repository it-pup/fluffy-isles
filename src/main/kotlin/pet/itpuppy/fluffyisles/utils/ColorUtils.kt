package pet.itpuppy.fluffyisles.utils

object ColorUtils {
    fun colorOf(r: Int, g: Int, b: Int, a: Int = 255): Int {
        return (a shl 24) or (r shl 16) or (g shl 8) or b
    }
}