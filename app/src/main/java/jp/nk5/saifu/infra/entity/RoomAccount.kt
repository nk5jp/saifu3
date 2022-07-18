package jp.nk5.saifu.infra.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.nk5.saifu.domain.Account

@Entity(tableName = "accounts")
data class RoomAccount(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    override var name: String,
    override var amount: Int,
    @ColumnInfo(name = "is_valid") override var isValid: Boolean
)  : Account(name, amount, isValid)