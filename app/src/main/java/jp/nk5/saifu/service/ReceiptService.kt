package jp.nk5.saifu.service

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.ReceiptDetail
import jp.nk5.saifu.domain.TaxType
import jp.nk5.saifu.domain.Title
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.domain.repository.ReceiptRepository
import jp.nk5.saifu.viewmodel.receipt.ReceiptViewModel

class ReceiptService(
    val accountRepository: AccountRepository,
    val receiptRepository: ReceiptRepository,
    val viewModel: ReceiptViewModel
) {

    suspend fun initializeView() {
        val accounts = accountRepository.getValidAccounts()
        viewModel.initializeViewModel(
            MyDate.today(),
            accounts
        )
    }

    suspend fun addDetail(title: Title, amount: Int) {
        viewModel.addItem(
            ReceiptDetail(
                viewModel.getNewDetailId(),
                title,
                amount,
                TaxType.INCLUDE
            )
        )
    }

}