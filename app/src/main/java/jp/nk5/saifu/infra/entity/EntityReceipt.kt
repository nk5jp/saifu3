package jp.nk5.saifu.infra.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class EntityReceipt(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: Int,
    @ColumnInfo(name = "account_id") val accountId: Int,
)