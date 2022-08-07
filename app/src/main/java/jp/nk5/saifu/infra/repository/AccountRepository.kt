package jp.nk5.saifu.infra.repository

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.infra.AppDatabase
import jp.nk5.saifu.infra.entity.EntityAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository (private val db: AppDatabase)
    : jp.nk5.saifu.domain.repository.AccountRepository {

    /**
     * 最初に呼び出されたタイミングでDBから全情報を取得。以後は差分処理を行う
     */
    private val lists by lazy {
        db.accountDao().selectAll().map { e ->
            Account(
                e.id,
                e.name,
                e.amount,
                e.isValid
            )
        }.toMutableList()
    }

    /**
     * 既存の口座を作成もしくは更新する処理
     * CreateかUpdateかは、idが0か否かで判定する（Roomの仕様にも基づく）
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     */
    override suspend fun setAccount(account: Account):Unit = withContext(Dispatchers.IO) {
        /**
         * 新規の場合
         */
        if (account.id == 0) {
            val id = db.accountDao().insertAccount(
                EntityAccount(
                    0,
                    account.name,
                    account.amount,
                    account.isValid
                )
            ).toInt()
            account.id = id
            lists.add(account)
        } else {
            db.accountDao().updateAccount(
                EntityAccount(
                    account.id,
                    account.name,
                    account.amount,
                    account.isValid
                )
            )
        }
    }

    /**
     * Mapperからドメイン要素を抽出したリストを作成して返却する
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     */
    override suspend fun getAccounts(): MutableList<Account> = withContext(Dispatchers.IO) {
        lists
    }

    /**
     * idに合致するaccountを返却する
     * この処理は例外をスローする可能性がある
     */
    override fun getAccountById(id: Int): Account {
        val account = lists.find { it.id == id }
        return account ?: throw Exception("this account does not exists on DB.")
    }
}