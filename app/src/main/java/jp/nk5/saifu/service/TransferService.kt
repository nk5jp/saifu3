package jp.nk5.saifu.service

import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.viewmodel.TransferViewModel

class TransferService(
    private val repository: AccountRepository,
    private val viewModel: TransferViewModel
) {

    suspend fun initializeView() {
        viewModel.updateList(repository.getValidAccounts())
    }


}