package jp.nk5.saifu.viewmodel.search

import jp.nk5.saifu.viewmodel.UpdateType

enum class SearchDetailUpdateType: UpdateType {
    LIST_UPDATE, //recyclerViewを全て再描画する
    SPINNER_AS_ACCOUNT, //spinner1に初期値を設定する
    BUTTON_AS_FROM, //button2の文字列を「From：yyyy/mm/dd」にする
    BUTTON_AS_TO, //button1の文字列を「To：yyyy/mm/dd」にする
    TEXT_AS_TOTAL //textView1の文字列を「総費用：xxx円 総収入：xxx円」にする
}