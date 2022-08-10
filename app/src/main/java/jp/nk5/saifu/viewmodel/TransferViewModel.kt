package jp.nk5.saifu.viewmodel

import jp.nk5.saifu.domain.Account

class TransferViewModel: MyViewModel() {
    val accounts = mutableListOf<Account>()
    val selectedPositions = mutableListOf<Int>()

    /**
     * リストを空にしてインスタンスを詰め直し、再描画を通知する
     * recyclerViewがインスタンスを参照しているのでこの方式にしている
     */
    suspend fun updateList(newAccounts: List<Account>) {
        selectedPositions.clear()
        accounts.clear()
        for (account in newAccounts) {
            accounts.add(account)
        }
        val types = listOf(
            TransferUpdateType.LIST_UPDATE
        )
        notifyObservers(types)
    }

}