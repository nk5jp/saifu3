package jp.nk5.saifu.infra.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import jp.nk5.saifu.infra.entity.EntityTransfer

@Dao
interface TransferDao {
    @Query("SELECT * FROM transfers " +
            "Where date >= (:yearMonth * 100) and date < ((:yearMonth + 1) * 100)")
    fun selectByYearMonth(yearMonth: Int): List<EntityTransfer>

    @Query("SELECT * FROM transfers " +
            "Where date >= :fromYmd and date <= :toYmd")
    fun selectByDuration(fromYmd: Int, toYmd: Int): List<EntityTransfer>

    @Insert
    fun insertTransfer(transfer: EntityTransfer): Long
}