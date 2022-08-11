package jp.nk5.saifu.service

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Transfer
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.domain.repository.TransferRepository
import jp.nk5.saifu.viewmodel.TransferViewModel

class TransferService(
    private val accountRepository: AccountRepository,
    private val transferRepository: TransferRepository,
    private val viewModel: TransferViewModel
) {

    /**
     * viewModelに最新の有効口座と金額を共有し、画面を再描画する（後者の発火はviewModelの責務）
     */
    suspend fun initializeView() {
        viewModel.updateList(accountRepository.getValidAccounts())
    }

    /**
     * 口座を選択し、ビューモデルに反映する。
     */
    suspend fun selectAccount(newPosition: Int) {
        viewModel.selectListItem(newPosition)
    }

    /**
     *
     */
    suspend fun transfer(amount: Int) {
        when {
            //入金処理
            viewModel.isPayment() -> {
                val debit = viewModel.getDebitAccount()
                debit.amount += amount
                accountRepository.setAccount(debit)
                transferRepository.setTransfer(
                    Transfer(
                        0,
                        MyDate.today(),
                        debit,
                        null,
                        amount
                    )
                )
                viewModel.updateList()
            }
            //振替処理
            viewModel.isTransfer() -> {
                val debit = viewModel.getDebitAccount()
                val credit = viewModel.getCreditAccount()
                debit.amount += amount
                credit.amount -= amount
                accountRepository.setAccount(debit)
                accountRepository.setAccount(credit)
                transferRepository.setTransfer(
                    Transfer(
                        0,
                        MyDate.today(),
                        debit,
                        credit,
                        amount
                    )
                )
                viewModel.updateList()
            }
            else -> {
                throw Exception("viewModel内で口座が3つ以上選択されています")
            }
        }
    }

}