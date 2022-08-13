package jp.nk5.saifu.service

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.ReceiptDetail
import jp.nk5.saifu.domain.TaxType
import jp.nk5.saifu.domain.Title
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.domain.repository.ReceiptRepository
import jp.nk5.saifu.viewmodel.receipt.ReceiptViewModel

class ReceiptService(
    val accountRepository: AccountRepository,
    val receiptRepository: ReceiptRepository,
    val viewModel: ReceiptViewModel
) {

    /**
     * viewModelに最新の有効口座一覧を共有し、画面を再描画する（後者はviewModelの責務）
     */
    suspend fun initializeView() {
        val accounts = accountRepository.getValidAccounts()
        viewModel.initializeViewModel(
            MyDate.today(),
            accounts
        )
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
     * 本処理は仕様バグの可能性があるため、物理レシートとの合計値の合致確認を徹底すること
     * 戻り値は要素数が2のリスト。1つ目が税込合計金額、2つ目が内税
     */
    fun sum(): List<Int> {
        //税込価格の合計値
        val includeSum = viewModel.details
            .filter{ it.taxType == TaxType.INCLUDE }
            .sumOf{ it.amount }
        //税抜価格（8%）の合計値
        var excludeEightSum = viewModel.details
            .filter { it.taxType == TaxType.EXCLUDE_EIGHT }
            .sumOf { it.amount }
        //税額の計算と合計値への加算
        val excludeEightTax = (excludeEightSum * 8 / 100)
        excludeEightSum += excludeEightTax
        //税抜価格（10%）の合計値
        var excludeTenSum = viewModel.details
            .filter { it.taxType == TaxType.EXCLUDE_TEN }
            .sumOf { it.amount }
        //税額の計算と合計値への加算
        val excludeTenTax = (excludeTenSum * 10 / 100)
        excludeTenSum += excludeTenTax
        return listOf(
            includeSum + excludeEightSum + excludeTenSum,
            excludeEightTax + excludeTenTax
        )
    }

}