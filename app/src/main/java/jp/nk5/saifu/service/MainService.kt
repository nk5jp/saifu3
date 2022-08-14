package jp.nk5.saifu.service

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.domain.repository.ReceiptRepository
import jp.nk5.saifu.viewmodel.main.MainViewModel

class MainService(
    private val receiptRepository: ReceiptRepository,
    private val accountRepository: AccountRepository,
    private val viewModel: MainViewModel
) {

    /**
     * 指定した年月日のレシートを取得し、画面の再描画を実施する（後者はviewModelの責務）
     */
    suspend fun updateView(date: MyDate) {
        val receipts = receiptRepository.getReceiptByYmd(date)
        viewModel.updateList(receipts, date)
    }

    /**
     * viewModelで保持している日付（初期値は本日）のレシートを取得し、
     * 画面の再描画を実施する（後者はviewModelの責務）
     */
    suspend fun initializeView() {
        val receipts = receiptRepository.getReceiptByYmd(viewModel.date)
        viewModel.updateList(receipts)
    }

    /**
     * 指定した行のレシートを削除した上でリストを再取得し、画面の再描画を実施する（後者はviewModelの責務）
     */
    suspend fun deleteReceipt(position: Int) {
        val receipt = viewModel.receipts[position]
        //削除分に相当する金額を口座に戻す
        receipt.account.amount += receipt.sum()[0]
        accountRepository.setAccount(receipt.account)
        //レシートをDBから削除する
        receiptRepository.deleteReceipt(viewModel.receipts[position])
        //レシート一覧を再取得してビューに貼り付ける
        val receipts = receiptRepository.getReceiptByYmd(viewModel.date)
        viewModel.updateList(receipts, viewModel.date)
    }

}