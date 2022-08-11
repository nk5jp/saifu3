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
            TransferUpdateType.LIST_UPDATE,
            TransferUpdateType.TEXTVIEW_AS_UNSELECTED,
            TransferUpdateType.TEXTVIEW_AS_SUM
        )
        notifyObservers(types)
    }

    /**
     * 既存のリストをそのまま用いて再描画を通知する
     * 振替処理後にわざわざリポジトリからリストを再取得する理由がないためこのように処理する
     */
    suspend fun updateList() {
        selectedPositions.clear()
        val types = listOf(
            TransferUpdateType.LIST_UPDATE,
            TransferUpdateType.TEXTVIEW_AS_UNSELECTED,
            TransferUpdateType.TEXTVIEW_AS_SUM
        )
        notifyObservers(types)
    }

    /**
     * viewModelの情報および新たに選択された行番号を踏まえ、リストの再描画およびUIの表示を修正する
     * selectedPositionsは要素0が借方、要素1が貸方を意味する
     */
    suspend fun selectListItem(newPosition: Int) {
        when {
            //既に選択されている行を再選択した場合：選択を解除する、借方・貸方の関係が変わる可能性もあるが許容する
            newPosition in selectedPositions -> {
                selectedPositions.remove(newPosition)
            }
            //新しい行で初選択もしくは2つ目の選択の場合：選択行を追加する
            selectedPositions.size < 2 -> {
                selectedPositions.add(newPosition)
            }
            //2つ選択されている状況で新たな行を選択した場合：何もせず処理を終了する
            else -> {
                return
            }
        }
        val types: List<TransferUpdateType> = when (selectedPositions.size) {
            //行が全く選択されていない場合の画面更新命令群
            0 -> {
                listOf(
                    TransferUpdateType.LIST_UPDATE,
                    TransferUpdateType.EDIT_CLEAR,
                    TransferUpdateType.TEXTVIEW_AS_UNSELECTED
                )
            }
            //行が1つ選択されている場合の画面更新命令群
            1 -> {
                listOf(
                    TransferUpdateType.LIST_UPDATE,
                    TransferUpdateType.EDIT_CLEAR,
                    TransferUpdateType.TEXTVIEW_AS_PAYMENT
                )
            }
            //行が2つ選択されている場合の画面更新命令群
            2 -> {
                listOf(
                    TransferUpdateType.LIST_UPDATE,
                    TransferUpdateType.EDIT_CLEAR,
                    TransferUpdateType.TEXTVIEW_AS_TRANSFER
                )
            }
            //それ以外
            else -> {
                throw Exception("3行が選択されています。これは想定していない状態です。")
            }
        }
        notifyObservers(types)
    }

    /**
     * 借方に相当するアカウント情報を返却する
     */
    fun getDebitAccount(): Account {
        return accounts[selectedPositions[0]]
    }

    /**
     * 貸方に相当するアカウント情報を返却する
     */
    fun getCreditAccount(): Account {
        return accounts[selectedPositions[1]]
    }

    /**
     * 口座が未選択か否かを返却する（未選択がtrueである）
     */
    fun isUnselected(): Boolean {
        return selectedPositions.size == 0
    }

    /**
     * 口座を1つ選択している（入金モードである）か否かを返却する
     */
    fun isPayment(): Boolean {
        return selectedPositions.size == 1
    }

    /**
     * 口座を2つ選択している（振替モードである）か否かを返却する
     */
    fun isTransfer(): Boolean {
        return selectedPositions.size == 2
    }

}