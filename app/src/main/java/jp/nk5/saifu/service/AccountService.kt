package jp.nk5.saifu.service

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.viewmodel.AccountViewModel

class AccountService(
    private val repository: AccountRepository,
    private val viewModel: AccountViewModel
) {

    /**
     * 新たなアカウントを開設し、ビューモデルに反映する。
     */
    suspend fun createAccount(name: String) {
        repository.setAccount(Account(name))
    }

}