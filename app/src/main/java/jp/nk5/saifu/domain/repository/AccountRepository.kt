package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.Account

/**
 * Accountを管理するRepository
 */
interface AccountRepository {
    suspend fun setAccount(account: Account)
    suspend fun getAccounts(): MutableList<Account>
    suspend fun getValidAccounts(): MutableList<Account>
    fun getAccountById(id: Int): Account
}