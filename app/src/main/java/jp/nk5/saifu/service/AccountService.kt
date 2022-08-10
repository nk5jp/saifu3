package jp.nk5.saifu.service

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.viewmodel.AccountViewModel

class AccountService(
    private val repository: AccountRepository,
    private val viewModel: AccountViewModel
) {

    /**
     * 新たな口座を開設し、ビューモデルに反映する。
     */
    suspend fun createAccount(name: String) {
        repository.setAccount(Account(name))
        viewModel.updateList(repository.getValidAccounts())
    }

    /**
     * 指定した口座を論理削除し、ビューモデルに反映する。
     */
    suspend fun deleteAccount(position: Int) {
        val account = viewModel.accounts[position]
        account.isValid = false
        repository.setAccount(account)
        viewModel.updateList(repository.getValidAccounts())
    }

}