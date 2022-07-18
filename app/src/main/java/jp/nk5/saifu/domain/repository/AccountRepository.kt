package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.Account

interface AccountRepository {
    fun setAccount(account: Account)
    fun getAccounts(): List<Account>
}