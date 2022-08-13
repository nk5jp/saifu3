package jp.nk5.saifu.infra.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "receipt_details",
    foreignKeys = [ForeignKey(
        entity = EntityReceipt::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("receipt_id"),
        onDelete = ForeignKey.CASCADE
    )],
    primaryKeys = ["receipt_id", "detail_id"]
)
data class EntityReceiptDetail(
        @ColumnInfo(name = "receipt_id") val receiptId: Int,
        @ColumnInfo(name = "detail_id") val detailId: Int,
        @ColumnInfo(name = "title_id") val titleId: Int,
        @ColumnInfo(name = "tax_type_id") val taxTypeId: Int,
        val amount: Int
)