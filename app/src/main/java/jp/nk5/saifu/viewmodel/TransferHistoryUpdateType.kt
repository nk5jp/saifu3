package jp.nk5.saifu.viewmodel

enum class TransferHistoryUpdateType: UpdateType {
    LIST_UPDATE, //RecyclerViewを全て再描画する
    BUTTON_AS_DATE //button1の文字列を「yyyy/mm」にする
}