package jp.nk5.saifu.service

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.viewmodel.account.AccountViewModel

class AccountService(
    private val repository: AccountRepository,
    private val viewModel: AccountViewModel
) {

    /**
     * viewModelに最新の有効口座を共有し、画面を再描画する（後者の発火はviewModelの責務）
     */
    suspend fun updateView() {
        viewModel.updateList(repository.getValidAccounts())
    }

    /**
     * 指定した口座を論理削除し、updateViewをキックする
     */
    suspend fun deleteAccount(position: Int) {
        val account = viewModel.getAccountByPosition(position)
        account.isValid = false
        repository.setAccount(account)
        updateView()
    }

    /**
     * viewModelの情報および入力口座名を踏まえて口座一覧を更新し、updateViewをキックする
     */
    suspend fun updateAccount(name: String) {
        val account: Account = if (viewModel.isSelected()) {
            //行が選択されている場合：その行の口座のインスタンスを名称だけ変更して返却する
            val selectingAccount = viewModel.getSelectingAccount()
            selectingAccount.name = name
            selectingAccount
        } else {
            //行が選択されていない場合：新たに口座を開設して返却する
            Account(name)
        }
        repository.setAccount(account)
        updateView()
    }

    /**
     * 口座を選択し、ビューモデルに反映する。
     */
    suspend fun selectAccount(newPosition: Int) {
        viewModel.selectListItem(newPosition)
    }

}