package jp.nk5.saifu.viewmodel.search

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Receipt
import jp.nk5.saifu.viewmodel.MyViewModel

class SearchDetailViewModel: MyViewModel() {
    val receipts = mutableListOf<Receipt>()
    var fromDate = MyDate.today()
    var toDate = MyDate.today()
    var loss = 0
    var profit = 0
    val accounts = mutableListOf<Account>()

    /**
     * 口座一覧の初期化を行い、画面に更新を通知する
     */
    suspend fun initializeView(accounts: List<Account>) {
        for (account in accounts) this.accounts.add(account)
        val types = listOf(
            SearchDetailUpdateType.TEXT_AS_TOTAL,
            SearchDetailUpdateType.SPINNER_AS_ACCOUNT,
            SearchDetailUpdateType.BUTTON_AS_FROM,
            SearchDetailUpdateType.BUTTON_AS_TO
        )
        notifyObservers(types)
    }

    suspend fun updateView(receipts: List<Receipt>) {
        this.receipts.clear()
        for (receipt in receipts) this.receipts.add(receipt)
        val types = listOf(
            SearchDetailUpdateType.LIST_UPDATE,
            SearchDetailUpdateType.TEXT_AS_TOTAL,
            SearchDetailUpdateType.BUTTON_AS_FROM,
            SearchDetailUpdateType.BUTTON_AS_TO
        )
        notifyObservers(types)
    }

}