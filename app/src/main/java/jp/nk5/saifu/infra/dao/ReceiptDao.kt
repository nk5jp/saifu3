package jp.nk5.saifu.infra.dao

import androidx.room.*
import jp.nk5.saifu.infra.entity.EntityReceipt
import jp.nk5.saifu.infra.entity.EntityReceiptDetail

@Dao
interface ReceiptDao {

    @Query("select * from receipts " +
            "inner join receipt_details on receipts.id = receipt_details.receipt_id" +
            "where receipts.date = :ymd")
    fun selectByYearMonth(ymd: Int): Map<EntityReceipt, List<EntityReceiptDetail>>

    @Insert
    fun insertReceipt(receipt: EntityReceipt): Long
    @Insert
    fun insertReceiptDetail(detail: EntityReceiptDetail): Long

    @Update
    fun updateReceipt(receipt: EntityReceipt)
    @Update
    fun updateReceiptDetails(vararg details: EntityReceiptDetail)

    @Delete
    fun deleteReceipt(receipt: EntityReceipt)

}