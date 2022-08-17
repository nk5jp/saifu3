package jp.nk5.saifu.service

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.TaxType
import jp.nk5.saifu.domain.Title
import jp.nk5.saifu.domain.repository.ReceiptRepository
import jp.nk5.saifu.domain.repository.TransferRepository
import jp.nk5.saifu.ui.util.TitleListAdapter
import jp.nk5.saifu.viewmodel.search.SearchViewModel

class SearchService(
    private val receiptRepository: ReceiptRepository,
    private val transferRepository: TransferRepository,
    private val viewModel: SearchViewModel
) {

    /**
     * 費用科目別の合計値を計算し、viewModelに反映する
     */
    suspend fun updateView() {
        val receipts = receiptRepository.getReceiptByDuration(
            viewModel.fromDate,
            viewModel.toDate
        )
        val transfers = transferRepository.getTransferByDuration(
            viewModel.fromDate,
            viewModel.toDate
        )

        val titleRows = mutableListOf<TitleListAdapter.TitleRow>()

        //それぞれの科目の内税・外税8%・外税10%をタンキングするための中間map
        val sums = mutableMapOf<Title, Array<Int>>()
        for (title in Title.titles) sums[title] = arrayOf(0, 0, 0)

        for (receipt in receipts) {
            receipt.details.forEach {
                when (it.taxType) {
                    TaxType.INCLUDE -> sums[it.title]!![0] += it.amount
                    TaxType.EXCLUDE_EIGHT -> sums[it.title]!![1] += it.amount
                    TaxType.EXCLUDE_TEN -> sums[it.title]!![2] += it.amount
                }
            }
        }
        for (title in Title.titles) titleRows.add(
            TitleListAdapter.TitleRow(
                title,
                sums[title]!![0]
                        + (sums[title]!![1] * 1.08).toInt()
                        + (sums[title]!![2]* 1.1).toInt()
            )
        )

        //合計支出を算出する、レシート基準なので科目ごとの合計から数円ずれる可能性があるが許容する
        viewModel.loss = receipts.sumOf { it.sum()[0] }
        viewModel.profit = transfers.filter { it.credit == null }.sumOf { it.amount }

        viewModel.updateView(titleRows)
    }

    /**
     * viewModelに検索日付の下限を共有し、画面を更新する（後者はviewModelの責務）
     */
    suspend fun updateFromDate(date: MyDate) {
        viewModel.fromDate = date
        updateView()
    }

    /**
     * viewModelに検索日付の上限を共有し、画面を更新する（後者はviewModelの責務）
     */
    suspend fun updateToDate(date: MyDate) {
        viewModel.toDate = date
        updateView()
    }

}