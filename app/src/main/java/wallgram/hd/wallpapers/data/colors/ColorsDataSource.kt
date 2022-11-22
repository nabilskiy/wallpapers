package wallgram.hd.wallpapers.data.colors

interface ColorsDataSource {

    fun colors(): List<Color>

    class Base : ColorsDataSource {
        override fun colors(): List<Color> {
            return listOf(
                Color.Silver(),
                Color.Blue(),
                Color.Pink(),
                Color.Red(),
                Color.Gold(),
                Color.White(),
                Color.Black(),
                Color.Green()
            )
        }

    }

}