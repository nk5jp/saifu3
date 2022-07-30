package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.Receipt

interface ReceiptRepository {
    fun setReceipt(receipt: Receipt)
    fun getReceiptByYmd(year: Int, month: Int, day: Int): List<Receipt>
    fun deleteReceipt(receipt: Receipt)
}