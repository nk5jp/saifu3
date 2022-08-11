package jp.nk5.saifu.viewmodel

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.ReceiptDetail

class ReceiptViewModel: MyViewModel() {
    var account: Account? = null
    var details = mutableListOf<ReceiptDetail>()

}