package jp.nk5.saifu.viewmodel.account

import jp.nk5.saifu.viewmodel.UpdateType

enum class AccountUpdateType: UpdateType {
    LIST_UPDATE, //RecyclerViewを全て再描画する
    EDIT_CLEAR, //editText1をクリアする
    EDIT_INPUT, //editText1に口座名を反映する
    BUTTON_AS_CREATE, //button1の文字列を「開設」にする
    BUTTON_AS_UPDATE //button1の文字列を「変更」にする
}