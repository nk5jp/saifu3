package jp.nk5.saifu.infra.dao

import androidx.room.Dao
import androidx.room.Query
import jp.nk5.saifu.infra.entity.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAll(): List<Account>
}