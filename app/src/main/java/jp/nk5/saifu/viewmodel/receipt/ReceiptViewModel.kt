package jp.nk5.saifu.viewmodel.receipt

import jp.nk5.saifu.domain.*
import jp.nk5.saifu.viewmodel.MyViewModel

class ReceiptViewModel: MyViewModel() {
    var id = 0
    var date = MyDate(99991231)
    val details = mutableListOf<ReceiptDetail>()
    val accounts = mutableListOf<Account>()

    suspend fun initializeViewModel(date: MyDate, accounts: List<Account>
    ) {
        this.date = date
        for (account in accounts) this.accounts.add(account)
        val types = listOf(
            ReceiptUpdateType.LIST_UPDATE,
            ReceiptUpdateType.SPINNER_AS_ACCOUNT,
            ReceiptUpdateType.TEXT_AS_TOTAL
        )
        notifyObservers(types)
    }

}