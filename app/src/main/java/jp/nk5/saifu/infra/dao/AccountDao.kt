package jp.nk5.saifu.infra.dao

import androidx.room.Dao
import androidx.room.Query
import jp.nk5.saifu.infra.entity.EntityAccount

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    fun getAll(): List<EntityAccount>
}