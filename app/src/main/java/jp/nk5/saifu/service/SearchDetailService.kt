package jp.nk5.saifu.service

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

}