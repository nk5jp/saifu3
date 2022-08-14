package jp.nk5.saifu.viewmodel.main

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Receipt
import jp.nk5.saifu.viewmodel.MyViewModel

class MainViewModel: MyViewModel() {
    val receipts = mutableListOf<Receipt>()
    var date = MyDate.today()

    /**
     * リストを空にしてインスタンスを詰め直し、再描画を通知する
     * recyclerViewがインスタンスを参照しているのでこの方式にしている
     */
    suspend fun updateList(newReceipts: List<Receipt>, newDate: MyDate) {
        receipts.clear()
        for (receipt in newReceipts) {
            receipts.add(receipt)
        }
        date = newDate
        val types = listOf(
            MainUpdateType.LIST_UPDATE,
            MainUpdateType.BUTTON_AS_DATE
        )
        notifyObservers(types)
    }

    /**
     * リストを空にしてインスタンスを詰め直し、再描画を通知する、こちらは日付を更新しない版
     * recyclerViewがインスタンスを参照しているのでこの方式にしている
     */
    suspend fun updateList(newReceipts: List<Receipt>) {
        receipts.clear()
        for (receipt in newReceipts) {
            receipts.add(receipt)
        }
        val types = listOf(
            MainUpdateType.LIST_UPDATE,
            MainUpdateType.BUTTON_AS_DATE
        )
        notifyObservers(types)
    }

}