package jp.nk5.saifu.infra.repository

import android.content.Context
import androidx.room.Room
import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.infra.AppDatabase

class AccountRepository (private val db: AppDatabase) : jp.nk5.saifu.domain.repository.AccountRepository {

    /**
     * 最初に呼び出されたタイミングでDBから全情報を取得。以後は差分処理を行う
     */
    private val lists by lazy {
        db.accountDao().selectAll().map { e -> Account(e.name, e.amount, e.isValid)}.toMutableList()
    }

    override fun setAccount(account: Account) {
        lists.add(account)
    }

    override fun getAccounts(): List<Account> {
        return lists
    }
}