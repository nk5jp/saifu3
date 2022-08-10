package jp.nk5.saifu.viewmodel

import jp.nk5.saifu.domain.Account

class AccountViewModel(
    val accounts: MutableList<Account>,
): MyViewModel() {
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
            AccountUpdateType.LIST_UPDATE,
            AccountUpdateType.EDIT_CLEAR,
            AccountUpdateType.BUTTON_AS_CREATE
        )
        notifyObservers(types)
    }

    /**
     * 選択行を記憶しておき、リストの再描画およびUIの表示を修正する
     * 単一選択画面ではあるが、本リストビューを流用する都合上、リストで管理している
     */
    suspend fun selectListItem(position: Int) {
        selectedPositions.add(position)
        val types = listOf(
            AccountUpdateType.LIST_UPDATE,
            AccountUpdateType.EDIT_INPUT,
            AccountUpdateType.BUTTON_AS_UPDATE
        )
        notifyObservers(types)
    }

    /**
     * 選択を解除し、リストの再描画およびUIの表示を修正する
     * 単一選択画面ではあるが、本リストビューを流用する都合上、リストで管理している
     */
    suspend fun unselectListItem(position: Int) {
        selectedPositions.remove(position)
        val types = listOf(
            AccountUpdateType.LIST_UPDATE,
            AccountUpdateType.EDIT_CLEAR,
            AccountUpdateType.BUTTON_AS_CREATE
        )
        notifyObservers(types)
    }

    /**
     * 選択を変更し、リストの再描画およびUIの表示を修正する
     * 単一選択画面ではあるが、本リストビューを流用する都合上、リストで管理している
     */
    suspend fun changeListItem(position: Int) {
        selectedPositions.clear()
        selectedPositions.add(position)
        val types = listOf(
            AccountUpdateType.LIST_UPDATE,
            AccountUpdateType.EDIT_INPUT
        )
        notifyObservers(types)
    }

    /**
     * 指定した位置の口座のインスタンスを返却する
     */
    fun getAccountByPosition(position: Int): Account {
        return accounts[position]
    }

    /**
     * 現在選択している口座位置を返却する
     */
    fun getSelectingPosition(): Int {
        return selectedPositions[0]
    }

    /**
     * 口座を選択状態にあるかを返却する
     */
    fun isSelected(): Boolean {
        return selectedPositions.size == 1
    }

    /**
     * 選択している口座を返却する
     */
    fun getSelectedAccount(): Account {
        return accounts[selectedPositions[0]]
    }

}