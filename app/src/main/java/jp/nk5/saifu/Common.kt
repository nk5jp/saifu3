package jp.nk5.saifu

import android.app.Application
import androidx.room.Room
import jp.nk5.saifu.infra.AppDatabase
import jp.nk5.saifu.infra.repository.AccountRepository
import jp.nk5.saifu.infra.repository.TransferRepository

/**
 * フラグメントを跨いで取得する必要がある情報を格納するレイヤ。
 * 以下を参照して実装。
 * https://aresei-note.com/611#toc3
 */
class Common: Application() {

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java, "saifu3-db"
        ).build()
    }

    val accountRepository by lazy { AccountRepository(db) }
    val transferRepository by lazy { TransferRepository(db, accountRepository) }

}