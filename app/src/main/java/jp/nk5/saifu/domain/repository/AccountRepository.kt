package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.Account

interface AccountRepository {
    fun setAccount(name: String, amount: Int, isValid: Boolean)
    fun setAccount(account: Account, name: String, amount: Int, isValid: Boolean)
    fun getAccounts(): List<Account>
}