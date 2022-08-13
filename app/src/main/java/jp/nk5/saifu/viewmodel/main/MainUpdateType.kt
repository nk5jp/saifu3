package jp.nk5.saifu.viewmodel.main

import jp.nk5.saifu.viewmodel.UpdateType

enum class MainUpdateType: UpdateType {
    LIST_UPDATE, //RecyclerViewを全て再描画する
    BUTTON_AS_DATE //button1の文字列を「yyyy/mm/dd」にする
}