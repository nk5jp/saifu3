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
        val account = viewModel.getAccountByPosition(position)
        account.isValid = false
        repository.setAccount(account)
        viewModel.updateList(repository.getValidAccounts())
    }

    /**
     * 指定した口座名を更新し、ビューモデルに反映する。
     */
    suspend fun updateAccount(position: Int, name: String) {
        val account = viewModel.getAccountByPosition(position)
        account.name = name
        repository.setAccount(account)
        viewModel.updateList(repository.getValidAccounts())
    }

    /**
     * 口座を選択し、ビューモデルに反映する。
     */
    suspend fun selectAccount(position: Int) {
        viewModel.selectListItem(position)
    }

    /**
     * 口座の選択を解除し、ビューモデルに反映する。
     */
    suspend fun unselectAccount(position: Int) {
        viewModel.unselectListItem(position)
    }

    /**
     * 別の口座を選択し、ビューモデルに反映する。
     */
    suspend fun changeAccount(position: Int) {
        viewModel.changeListItem(position)
    }

}