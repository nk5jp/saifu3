package jp.nk5.saifu.service

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.domain.repository.ReceiptRepository
import jp.nk5.saifu.domain.repository.TransferRepository
import jp.nk5.saifu.viewmodel.search.SearchDetailViewModel

class SearchDetailService(
    private val accountRepository: AccountRepository,
    private val receiptRepository: ReceiptRepository,
    private val transferRepository: TransferRepository,
    private val viewModel: SearchDetailViewModel
) {

    /**
     * 口座の一覧を取得し、viewModelに共有する
     */
    suspend fun initializeView() {
        val accounts = accountRepository.getValidAccounts()
        viewModel.initializeView(accounts)
    }

    /**
     * レシートの一覧を入手し、画面を更新する。
     */
    suspend fun updateView(account: Account) {
        //対象期間のレシートを抽出
        val allReceipts = receiptRepository.getReceiptByDuration(
            viewModel.fromDate,
            viewModel.toDate
        )
        //対象口座を使用した購買のみを抽出
        val receipts = allReceipts.filter { it.account == account }.toMutableList()
        //合計支出を算出して共有する
        viewModel.loss = receipts.sumOf { it.sum()[0] }

        //対象期間の振替を抽出
        val transfers = transferRepository.getTransferByDuration(
            viewModel.fromDate,
            viewModel.toDate
        )
        //対象口座への入金処理のみを抽出して合計金額をviewModelに反映
        viewModel.profit = transfers
            .filter { it.credit == null && it.debit == account }
            .toMutableList()
            .sumOf { it.amount }

        viewModel.updateView(receipts)
    }


    /**
     * viewModelに検索日付の下限を共有し、画面を更新する（後者はviewModelの責務）
     */
    suspend fun updateFromDate(date: MyDate, account: Account) {
        viewModel.fromDate = date
        updateView(account)
    }

    /**
     * viewModelに検索日付の上限を共有し、画面を更新する（後者はviewModelの責務）
     */
    suspend fun updateToDate(date: MyDate, account: Account) {
        viewModel.toDate = date
        updateView(account)
    }
}