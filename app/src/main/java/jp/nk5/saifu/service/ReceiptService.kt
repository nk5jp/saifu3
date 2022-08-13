package jp.nk5.saifu.service

import jp.nk5.saifu.domain.*
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.domain.repository.ReceiptRepository
import jp.nk5.saifu.viewmodel.receipt.ReceiptViewModel

class ReceiptService(
    val accountRepository: AccountRepository,
    private val receiptRepository: ReceiptRepository,
    val viewModel: ReceiptViewModel
) {

    /**
     * viewModelに最新の有効口座一覧を共有し、画面を再描画する（後者はviewModelの責務）
     */
    suspend fun initializeView(date: MyDate, id: Int) {
        val accounts = accountRepository.getValidAccounts()
        if (id == 0) {
            //新規作成の場合
            viewModel.initializeViewModel(
                date,
                accounts
            )
        } else {
            //修正の場合
            val receipt = receiptRepository.getReceiptById(id)
            viewModel.initializeViewModel(
                receipt,
                accounts
            )
        }

    }

    /**
     * viewModelに新たな明細を共有し、画面を再描画する（後者はviewModelの責務）
     */
    suspend fun addDetail(title: Title, amount: Int) {
        viewModel.addItem(
            ReceiptDetail(
                viewModel.getNewDetailId(),
                title,
                amount,
                TaxType.INCLUDE
            )
        )
    }

    /**
     * 対象明細の税種別の変更を依頼する
     * 内税⇒外税8%⇒外税10%⇒内税の順番に変更する
     */
    suspend fun changeTaxType(position: Int) {
        viewModel.changeTaxType(position)
    }

    /**
     * 対象明細の削除を依頼する
     */
    suspend fun deleteDetail(position: Int) {
        viewModel.deleteDetail(position)
    }

    suspend fun updateReceipt(account: Account) {
        val receipt = Receipt(
            viewModel.id,
            viewModel.date,
            account,
            viewModel.details
        )
        val sum = receipt.sum()[0]
        when (viewModel.originalAccount) {
            //新規作成の場合
            null -> {
                account.amount -= sum
            }
            //同じ口座を使った修正処理の場合
            account -> {
                account.amount -= (sum - viewModel.originalSum)
            }
            //異なる口座を使った修正処理の場合
            else -> {
                viewModel.originalAccount!!.amount += viewModel.originalSum
                account.amount -= sum
                accountRepository.setAccount(viewModel.originalAccount!!)
            }
        }
        //金額更新後の口座をDBに保存する
        accountRepository.setAccount(account)
        //レシートおよび明細をDBに保存する
        receiptRepository.setReceipt(receipt)
        viewModel.updateReceipt()
    }

    /**
     * viewModelの内容を踏まえて合計金額を算出する。
     * 本処理は仕様バグの可能性があるため、物理レシートとの合計値の合致確認を徹底すること
     * 戻り値は要素数が2のリスト。1つ目が税込合計金額、2つ目が内税
     */
    fun sum(): List<Int> {
        return Receipt.sum(viewModel.details)
    }

    /**
     * 結果ダイアログに表示する文字列を返却する
     */
    fun getResultMessage(account: Account): String {
        return when (viewModel.originalAccount) {
            //新規作成の場合
            null -> {
                "%s：%,d円".format(account.name, account.amount)
            }
            //同じ口座を用いた修正の場合
            account -> {
                "%s：%,d円".format(account.name, account.amount)
            }
            //別の口座を用いた修正の場合
            else -> {
                "%s：%,d円　%s：%,d円".format(
                    account.name,
                    account.amount,
                    viewModel.originalAccount!!.name,
                    viewModel.originalAccount!!.amount,
                )
            }
        }
    }

}