package jp.nk5.saifu.viewmodel.transfer

import jp.nk5.saifu.viewmodel.UpdateType

enum class TransferHistoryUpdateType: UpdateType {
    LIST_UPDATE, //RecyclerViewを全て再描画する
    BUTTON_AS_DATE //button1の文字列を「yyyy/mm」にする
}