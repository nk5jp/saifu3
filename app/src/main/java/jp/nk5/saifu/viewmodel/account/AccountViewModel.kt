package jp.nk5.saifu.viewmodel.account

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.viewmodel.MyViewModel

class AccountViewModel: MyViewModel() {
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
            AccountUpdateType.LIST_UPDATE,
            AccountUpdateType.EDIT_CLEAR,
            AccountUpdateType.BUTTON_AS_CREATE
        )
        notifyObservers(types)
    }

    /**
     * viewModelの情報および新たに選択された行番号を踏まえ、リストの再描画およびUIの表示を修正する
     * この画面は単一選択画面ではあるが、アダプターを流用する都合上、リストで管理している
     */
    suspend fun selectListItem(newPosition: Int) {
        if (isSelected() && selectedPositions[0] == newPosition) {
            //既に選択されている行を再選択した場合：選択を解除する
            selectedPositions.clear()
        } else {
            //それ以外の場合：選択行を更新する
            selectedPositions.clear()
            selectedPositions.add(newPosition)
        }
        val types: List<AccountUpdateType> = if (isSelected()) {
            //行が選択されている場合の画面更新命令群
            listOf(
                AccountUpdateType.LIST_UPDATE,
                AccountUpdateType.EDIT_INPUT,
                AccountUpdateType.BUTTON_AS_UPDATE
            )
        } else {
            //行が選択されていない場合の画面更新命令群
            listOf(
                AccountUpdateType.LIST_UPDATE,
                AccountUpdateType.EDIT_CLEAR,
                AccountUpdateType.BUTTON_AS_CREATE
            )
        }
        notifyObservers(types)
    }

    /**
     * 指定した位置の口座のインスタンスを返却する
     */
    fun getAccountByPosition(position: Int): Account {
        return accounts[position]
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
    fun getSelectingAccount(): Account {
        return accounts[selectedPositions[0]]
    }

}