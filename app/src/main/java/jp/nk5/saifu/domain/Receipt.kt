package jp.nk5.saifu.domain

/**
 * レシートを意味するドメイン
 */
data class Receipt(val date: MyDate, var account: Account, val details: List<ReceiptDetail>) {
    /**
     * レシートに紐づく金額の合計値を返却する
     */
    fun getSum(): Int {
        return if(details.isEmpty())  0 else details.map{d -> d.amount}.sum()
    }
}
