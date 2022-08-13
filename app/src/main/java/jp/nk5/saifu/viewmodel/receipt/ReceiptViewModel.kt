package jp.nk5.saifu.viewmodel.receipt

import jp.nk5.saifu.domain.*
import jp.nk5.saifu.viewmodel.MyViewModel

class ReceiptViewModel: MyViewModel() {
    var id = 0
    var date = MyDate(99991231)
    var originalAccount: Account? = null
    var originalSum: Int = 0
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
     * 既存のレシート修正画面の初期化処理。
     * 作成日付を代入し、spinner用の口座一覧をセットしてから、再描画を通知する
     */
    suspend fun initializeViewModel(receipt: Receipt, accounts: List<Account>) {
        this.id = receipt.id
        this.date = receipt.date
        this.originalAccount = receipt.account
        this.originalSum = receipt.sum()[0]
        for (detail in receipt.details) this.details.add(detail)
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
     * 指定した位置の明細の勢種別を変更し、再描画を通知する
     * 内税⇒外税8%⇒外税10%⇒内税の順番に変更する
     */
    suspend fun changeTaxType(position: Int) {
        val detail = details[position]
        when (detail.taxType) {
            TaxType.INCLUDE -> detail.taxType = TaxType.EXCLUDE_EIGHT
            TaxType.EXCLUDE_EIGHT -> detail.taxType = TaxType.EXCLUDE_TEN
            TaxType.EXCLUDE_TEN -> detail.taxType = TaxType.INCLUDE
        }
        val types = listOf(
            ReceiptUpdateType.LIST_UPDATE,
            ReceiptUpdateType.TEXT_AS_TOTAL
        )
        notifyObservers(types)
    }

    /**
     * 指定した位置の明細を削除してidを再採番し、再描画を通知する
     */
    suspend fun deleteDetail(position: Int) {
        details.removeAt(position)
        //idが欠番になると追加処理などで不都合が生じ得るため、0から採番しなおす
        var newId = 0
        for (detail in details) detail.id = newId++
        val types = listOf(
            ReceiptUpdateType.LIST_UPDATE,
            ReceiptUpdateType.TEXT_AS_TOTAL
        )
        notifyObservers(types)
    }

    /**
     * 結果のダイアログを表示する
     */
    suspend fun updateReceipt() {
        val types = listOf(
            ReceiptUpdateType.DIALOG_SHOW
        )
        notifyObservers(types)
    }

    /**
     * 新たにdetailsを作成するときに付与すべきIDを返却する
     */
    fun getNewDetailId() = details.size
}