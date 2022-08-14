package jp.nk5.saifu.viewmodel.search

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.viewmodel.MyViewModel
import jp.nk5.saifu.ui.util.TitleListAdapter.TitleRow

class SearchViewModel: MyViewModel() {
    val titles = mutableListOf<TitleRow>()
    var fromDate = MyDate.today()
    var toDate = MyDate.today()
    var loss = 0
    var profit = 0

    /**
     * 受領した集計結果をリストに反映して、画面を再描画する
     */
    suspend fun updateView(titles: List<TitleRow>) {
        this.titles.clear()
        for (title in titles) this.titles.add(title)
        val types = listOf(
            SearchUpdateType.LIST_UPDATE,
            SearchUpdateType.BUTTON_AS_FROM,
            SearchUpdateType.BUTTON_AS_TO,
            SearchUpdateType.TEXT_AS_TOTAL
        )
        notifyObservers(types)
    }

}