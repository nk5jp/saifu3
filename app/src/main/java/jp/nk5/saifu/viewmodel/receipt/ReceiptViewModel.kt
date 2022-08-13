package jp.nk5.saifu.viewmodel.receipt

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.Receipt
import jp.nk5.saifu.domain.ReceiptDetail
import jp.nk5.saifu.domain.Title
import jp.nk5.saifu.viewmodel.MyViewModel

class ReceiptViewModel: MyViewModel() {
    private var _receipt: Receipt? = null
    val receipt get() = _receipt!!
    val accounts = mutableListOf<Account>()

    suspend fun initializeViewModel(receipt: Receipt, accounts: List<Account>) {
        _receipt = receipt
        for (account in accounts) this.accounts.add(account)
        val types = listOf(
            ReceiptUpdateType.LIST_UPDATE,
            ReceiptUpdateType.SPINNER_AS_ACCOUNT,
            ReceiptUpdateType.TEXT_AS_TOTAL
        )
        notifyObservers(types)
    }

}