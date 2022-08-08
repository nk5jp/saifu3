package jp.nk5.saifu.viewmodel

enum class AccountUpdateType: UpdateType {
    LIST_UPDATE, //RecyclerViewを再描画する
    LIST_SELECT, //RecyclerViewの要素を選択する
    EDIT_CLEAR, //editText1をクリアする
    EDIT_INPUT, //editText1をクリアする
    BUTTON_AS_CREATE, //button1の文字列を「開設」にする
    BUTTON_AS_UPDATE //button1の文字列を「変更」にする
}