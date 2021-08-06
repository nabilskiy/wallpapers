package wallgram.hd.wallpapers.util.modo

/**
 * Holder of current navigation state
 */
data class NavigationState(
    val chain: List<Screen> = emptyList()
)
