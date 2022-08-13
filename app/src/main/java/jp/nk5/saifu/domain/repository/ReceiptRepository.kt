package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Receipt

interface ReceiptRepository {
    suspend fun setReceipt(receipt: Receipt)
    suspend fun getReceiptByYmd(date: MyDate): MutableList<Receipt>
    suspend fun deleteReceipt(receipt: Receipt)
}