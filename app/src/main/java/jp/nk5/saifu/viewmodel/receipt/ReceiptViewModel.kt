package jp.nk5.saifu.viewmodel.receipt

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.ReceiptDetail
import jp.nk5.saifu.viewmodel.MyViewModel

class ReceiptViewModel: MyViewModel() {
    var account: Account? = null
    var details = mutableListOf<ReceiptDetail>()

}