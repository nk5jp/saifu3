package jp.nk5.saifu.infra.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transfers")
data class EntityTransfer (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: Int,
    @ColumnInfo(name = "debit_id") val debitId: Int,
    @ColumnInfo(name = "credit_id") val creditId: Int?,
    val amount: Int
)