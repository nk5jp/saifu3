package jp.nk5.saifu.infra.repository

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.infra.entity.RoomAccount

class RoomAccountRepository: AccountRepository {

    override fun setAccount(account: Account) {
        TODO("Not yet implemented")
    }

    override fun getAccounts(): List<Account> {
        val accounts: List<RoomAccount> = mutableListOf()
        return accounts
    }
}