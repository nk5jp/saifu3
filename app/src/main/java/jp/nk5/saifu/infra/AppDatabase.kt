package jp.nk5.saifu.infra

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.nk5.saifu.infra.dao.AccountDao
import jp.nk5.saifu.infra.entity.Account

@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}