package jp.nk5.saifu.domain

/**
 * レシートを意味するドメイン
 */
data class Receipt(
    var id: Int,
    val date: MyDate,
    var account: Account,
    val details: MutableList<ReceiptDetail>
) {
    /**
     * 当該インスタンスのdetailsを用いてcompanion objectのsum関数を実行する
     * 戻り値は要素数が2のリスト。1つ目が税込合計金額、2つ目が内税
     */
    fun sum(): List<Int> {
        return sum(this.details)
    }

    companion object {
        /**
         * viewModelの内容を踏まえて合計金額を算出する。
         * 本処理は仕様バグの可能性があるため、物理レシートとの合計値の合致確認を徹底すること
         * 戻り値は要素数が2のリスト。1つ目が税込合計金額、2つ目が内税
         */
        fun sum(details: List<ReceiptDetail>): List<Int> {
            val includeSum = details
                .filter{ it.taxType == TaxType.INCLUDE }
                .sumOf{ it.amount }
            //税抜価格（8%）の合計値
            var excludeEightSum = details
                .filter { it.taxType == TaxType.EXCLUDE_EIGHT }
                .sumOf { it.amount }
            //税額の計算と合計値への加算
            val excludeEightTax = (excludeEightSum * 8 / 100)
            excludeEightSum += excludeEightTax
            //税抜価格（10%）の合計値
            var excludeTenSum = details
                .filter { it.taxType == TaxType.EXCLUDE_TEN }
                .sumOf { it.amount }
            //税額の計算と合計値への加算
            val excludeTenTax = (excludeTenSum * 10 / 100)
            excludeTenSum += excludeTenTax
            return listOf(
                includeSum + excludeEightSum + excludeTenSum,
                excludeEightTax + excludeTenTax
            )
        }
    }
}
