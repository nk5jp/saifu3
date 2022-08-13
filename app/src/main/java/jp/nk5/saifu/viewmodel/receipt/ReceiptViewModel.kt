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
    val titles = mutableListOf<Title>()

    suspend fun initializeViewModel(
        receipt: Receipt, accounts: List<Account>, titles: List<Title>
    ) {
        _receipt = receipt
        for (account in accounts) this.accounts.add(account)
        for (title in titles) this.titles.add(title)
        val types = listOf(
            ReceiptUpdateType.LIST_UPDATE,
            ReceiptUpdateType.SPINNER_AS_ACCOUNT,
            ReceiptUpdateType.SPINNER_AS_TITLE,
            ReceiptUpdateType.TEXT_AS_TOTAL
        )
        notifyObservers(types)
    }

}