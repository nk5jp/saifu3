package jp.nk5.saifu.infra.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.nk5.saifu.domain.Account

@Entity(tableName = "accounts")
data class EntityAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val amount: Int,
    @ColumnInfo(name = "is_valid") val isValid: Boolean
)