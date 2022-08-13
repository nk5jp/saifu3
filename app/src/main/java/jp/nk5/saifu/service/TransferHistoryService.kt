package jp.nk5.saifu.service

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.repository.TransferRepository
import jp.nk5.saifu.viewmodel.transfer.TransferHistoryViewModel

class TransferHistoryService(
    private val repository: TransferRepository,
    private val viewModel: TransferHistoryViewModel
) {
    /**
     * viewModelに指定した年月の振替履歴を共有し、画面を再描画する（後者の発火はviewModelの責務）
     */
    suspend fun updateView(date: MyDate) {
        viewModel.updateList(
            repository.getTransferByYearMonth(date.getYm()),
            date
        )
    }
}