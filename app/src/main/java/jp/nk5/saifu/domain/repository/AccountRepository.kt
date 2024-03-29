package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.Account

/**
 * 口座を管理するRepository
 */
interface AccountRepository {
    suspend fun setAccount(account: Account)
    suspend fun getAccounts(): MutableList<Account>
    suspend fun getValidAccounts(): MutableList<Account>
    suspend fun getAccountById(id: Int): Account
}