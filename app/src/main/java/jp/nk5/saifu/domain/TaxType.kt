package jp.nk5.saifu.domain

/**
 * 税種別を意味するドメイン
 * 今後税種別が増えたり廃止された場合は要仕様検討
 */
enum class TaxType(val id: Int, val rate: Int) {
    INCLUDE(1, 0), //税込の金額
    EXCLUDE_EIGHT(2, 8), //税抜き8%の金額
    EXCLUDE_TEN(3, 10); //税抜き10%の金額

    companion object {
        fun getTaxTypeById(id: Int): TaxType {
            return when(id) {
                1 -> INCLUDE
                2 -> EXCLUDE_EIGHT
                3 -> EXCLUDE_TEN
                else -> throw Exception("存在しない税種別IDです")
            }
        }
    }
}