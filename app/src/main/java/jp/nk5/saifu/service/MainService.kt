package jp.nk5.saifu.service

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.repository.ReceiptRepository
import jp.nk5.saifu.viewmodel.main.MainViewModel

class MainService(
    private val repository: ReceiptRepository,
    private val viewModel: MainViewModel
) {

    /**
     * 指定した年月日のレシートを取得し、画面の再描画を実施する（後者はviewModelの責務）
     */
    suspend fun updateView(date: MyDate) {
        val receipts = repository.getReceiptByYmd(date)
        viewModel.updateList(receipts, date)
    }

}