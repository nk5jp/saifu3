package jp.nk5.saifu.infra.repository

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.infra.AppDatabase
import jp.nk5.saifu.infra.entity.EntityAccount

class AccountRepository (private val db: AppDatabase) : jp.nk5.saifu.domain.repository.AccountRepository {

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
     */
    override fun setAccount(account: Account) {
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
     */
    override fun getAccounts(): List<Account> {
        return lists
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