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
     * レシートに紐づく金額の合計値を返却する
     */
    fun getSum(): Int {
        return if(details.isEmpty())  0 else details.map{d -> d.amount}.sum()
    }
}
