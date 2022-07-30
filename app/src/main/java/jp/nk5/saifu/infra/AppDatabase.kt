package jp.nk5.saifu.infra

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.nk5.saifu.infra.dao.AccountDao
import jp.nk5.saifu.infra.dao.ReceiptDao
import jp.nk5.saifu.infra.dao.TransferDao
import jp.nk5.saifu.infra.entity.EntityAccount
import jp.nk5.saifu.infra.entity.EntityReceipt
import jp.nk5.saifu.infra.entity.EntityReceiptDetail
import jp.nk5.saifu.infra.entity.EntityTransfer

@Database(
    entities = [
        EntityAccount::class,
        EntityTransfer::class,
        EntityReceipt::class,
        EntityReceiptDetail::class
               ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transferDao(): TransferDao
    abstract fun receiptDao(): ReceiptDao
}