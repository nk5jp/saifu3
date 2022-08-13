package jp.nk5.saifu.service

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Receipt
import jp.nk5.saifu.domain.ReceiptDetail
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
            Receipt(0, MyDate.today(), accounts[0], mutableListOf()),
            accounts,
            listOf(Title.FOOD, Title.ENTERTAINMENT)
        )
    }

}