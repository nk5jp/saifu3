package jp.nk5.saifu.infra.dao

import androidx.room.*
import jp.nk5.saifu.infra.entity.EntityReceipt
import jp.nk5.saifu.infra.entity.EntityReceiptDetail

@Dao
interface ReceiptDao {

    @Query("select * from receipts inner join receipt_details on receipts.id = receipt_details.receipt_id where receipts.date = :ymd")
    fun selectByYearMonth(ymd: Int): Map<EntityReceipt, List<EntityReceiptDetail>>

    @Query("select * from receipts inner join receipt_details on receipts.id = receipt_details.receipt_id where receipts.id = :id")
    fun selectById(id: Int): Map<EntityReceipt, List<EntityReceiptDetail>>

    @Insert
    fun insertReceipt(receipt: EntityReceipt): Long
    @Insert
    fun insertReceiptDetail(detail: EntityReceiptDetail): Long

    @Update
    fun updateReceipt(receipt: EntityReceipt)

    @Delete
    fun deleteReceipt(receipt: EntityReceipt)

    @Query("delete from receipt_details where receipt_id = :id")
    fun deleteReceiptDetailsById(id: Int)

}