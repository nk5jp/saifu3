package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.Account

/**
 * Accountを管理するRepository
 */
interface AccountRepository {
    fun setAccount(account: Account)
    fun getAccounts(): List<Account>
    fun getAccountById(id: Int): Account
}