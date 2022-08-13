package jp.nk5.saifu.viewmodel.receipt

import jp.nk5.saifu.domain.*
import jp.nk5.saifu.viewmodel.MyViewModel

class ReceiptViewModel: MyViewModel() {
    var id = 0
    var date = MyDate(99991231)
    val details = mutableListOf<ReceiptDetail>()
    val accounts = mutableListOf<Account>()

    /**
     * 新規のレシート作成画面の初期化処理。
     * 作成日付を代入し、spinner用の口座一覧をセットしてから、再描画を通知する
     */
    suspend fun initializeViewModel(date: MyDate, accounts: List<Account>) {
        this.date = date
        for (account in accounts) this.accounts.add(account)
        val types = listOf(
            ReceiptUpdateType.LIST_UPDATE,
            ReceiptUpdateType.SPINNER_AS_ACCOUNT,
            ReceiptUpdateType.TEXT_AS_TOTAL
        )
        notifyObservers(types)
    }

    /**
     * 明細の一覧に新たなアイテムを設定し、再描画を通知する
     */
    suspend fun addItem(detail: ReceiptDetail) {
        details.add(detail)
        val types = listOf(
            ReceiptUpdateType.LIST_UPDATE,
            ReceiptUpdateType.EDIT_CLEAR,
            ReceiptUpdateType.TEXT_AS_TOTAL
        )
        notifyObservers(types)
    }

    /**
     * 新たにdetailsを作成するときに付与すべきIDを返却する
     */
    fun getNewDetailId() = details.size
}