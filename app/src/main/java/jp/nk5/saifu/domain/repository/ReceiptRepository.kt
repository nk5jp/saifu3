package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.Receipt

interface ReceiptRepository {
    suspend fun setReceipt(receipt: Receipt)
    suspend fun getReceiptByYmd(year: Int, month: Int, day: Int): MutableList<Receipt>
    suspend fun deleteReceipt(receipt: Receipt)
}