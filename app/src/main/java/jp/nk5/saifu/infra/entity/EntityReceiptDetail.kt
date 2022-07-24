package jp.nk5.saifu.infra.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_details",
    foreignKeys = [ForeignKey(
        entity = EntityReceipt::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("receiptId"),
        onDelete = ForeignKey.CASCADE
    )],
    primaryKeys = ["receiptId", "detailId"]
)
data class EntityReceiptDetail(
        @ColumnInfo(name = "receipt_id") val receiptId: Int,
        @ColumnInfo(name = "detail_id") val detailId: Int,
        @ColumnInfo(name = "title_id") val titleId: Int,
        val amount: Int
)