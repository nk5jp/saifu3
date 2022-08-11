package jp.nk5.saifu.service

import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.viewmodel.TransferViewModel

class TransferService(
    private val repository: AccountRepository,
    private val viewModel: TransferViewModel
) {

    /**
     * viewModelに最新の有効口座と金額を共有し、画面を再描画する（後者の発火はviewModelの責務）
     */
    suspend fun updateView() {
        viewModel.updateList(repository.getValidAccounts())
    }


    /**
     * 口座を選択し、ビューモデルに反映する。
     */
    suspend fun selectAccount(newPosition: Int) {
        viewModel.selectListItem(newPosition)
    }

}