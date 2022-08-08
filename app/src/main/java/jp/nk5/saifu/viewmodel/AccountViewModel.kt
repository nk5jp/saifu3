package jp.nk5.saifu.viewmodel

import jp.nk5.saifu.domain.Account

class AccountViewModel(
    val accounts: MutableList<Account>,
): MyViewModel() {
    val selectedPositions = mutableListOf<Int>()


    suspend fun updateList(newAccounts: List<Account>) {
        //リストを空にしてインスタンスを詰め直す、recyclerViewがインスタンスを参照しているのでこの方式にしている
        accounts.clear()
        for (account in newAccounts) {
            accounts.add(account)
        }
        val types = listOf(
            AccountUpdateType.LIST_UPDATE,
            AccountUpdateType.EDIT_CLEAR
        )
        notifyObservers(types)
    }



}