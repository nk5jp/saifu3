package jp.nk5.saifu.viewmodel.search

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Receipt
import jp.nk5.saifu.viewmodel.MyViewModel

class SearchDetailViewModel: MyViewModel() {
    private val receipts = mutableListOf<Receipt>()
    private var fromDate = MyDate.today()
    private var toDate = MyDate.today()

}