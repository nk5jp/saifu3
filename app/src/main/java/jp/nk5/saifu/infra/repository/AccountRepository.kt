package jp.nk5.saifu.infra.repository

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.infra.AppDatabase
import jp.nk5.saifu.infra.entity.EntityAccount
import jp.nk5.saifu.infra.mapper.MapperAccount

class AccountRepository (private val db: AppDatabase) : jp.nk5.saifu.domain.repository.AccountRepository {

    /**
     * 最初に呼び出されたタイミングでDBから全情報を取得。以後は差分処理を行う
     */
    private val lists by lazy {
        db.accountDao().selectAll().map { e -> MapperAccount(e.id, Account(e.name, e.amount, e.isValid))}.toMutableList()
    }

    /**
     * 新しい口座を登録する処理
     */
    override fun setAccount(name: String, amount: Int, isValid: Boolean) {
        val id = db.accountDao().insertAccount(EntityAccount(0, name, amount, isValid)).toInt()
        lists.add(MapperAccount(id, Account(name, amount, isValid)))
    }

    /**
     * 既存の口座を更新する処理
     */
    override fun setAccount(account: Account, name: String, amount: Int, isValid: Boolean) {
        db.accountDao().updateAccount(EntityAccount(getId(account), name, amount, isValid))
        account.name = name
        account.amount = amount
        account.isValid = isValid
    }

    /**
     * Mapperからドメイン要素を抽出したリストを作成して返却する
     */
    override fun getAccounts(): List<Account> {
        val result = mutableListOf<Account>()
        for (it in lists) result.add(it.getDomain())
        return result
    }

    private fun getId(account: Account): Int {
        val result = lists.find { it.getDomain() == account }
        return result?.id ?: 0
    }
}