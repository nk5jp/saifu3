package jp.nk5.saifu.infra.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jp.nk5.saifu.infra.entity.EntityAccount

/**
 * accountsテーブルに対するDAO
 * accountsに対しては論理削除しか行わないのでdeleteは用意しない
 */
@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    fun selectAll(): List<EntityAccount>

    @Insert
    fun insertAccount(account: EntityAccount): Long

    @Update
    fun updateAccount(account: EntityAccount)
}