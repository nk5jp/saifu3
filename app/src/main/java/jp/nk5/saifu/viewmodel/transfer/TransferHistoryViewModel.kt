package jp.nk5.saifu.viewmodel.transfer

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Transfer
import jp.nk5.saifu.viewmodel.MyViewModel

class TransferHistoryViewModel: MyViewModel() {
    val transfers = mutableListOf<Transfer>()
    var date = MyDate(99991231)

    /**
     * リストを空にしてインスタンスを詰め直し、再描画を通知する
     * recyclerViewがインスタンスを参照しているのでこの方式にしている
     */
    suspend fun updateList(newTransfers: List<Transfer>, newDate: MyDate) {
        transfers.clear()
        for(transfer in newTransfers) {
            transfers.add(transfer)
        }
        date = newDate
        val types = listOf(
            TransferHistoryUpdateType.BUTTON_AS_DATE,
            TransferHistoryUpdateType.LIST_UPDATE
        )
        notifyObservers(types)
    }

}