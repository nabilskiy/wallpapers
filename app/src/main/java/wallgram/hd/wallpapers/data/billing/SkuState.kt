package wallgram.hd.wallpapers.data.billing

data class SkuState(val state: State, val date: Long = 0L){

}

enum class State {
    SKU_STATE_UNPURCHASED, SKU_STATE_PENDING, SKU_STATE_PURCHASED, SKU_STATE_PURCHASED_AND_ACKNOWLEDGED
}