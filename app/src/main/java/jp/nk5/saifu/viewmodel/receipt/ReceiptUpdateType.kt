package jp.nk5.saifu.viewmodel.receipt

import jp.nk5.saifu.viewmodel.UpdateType

enum class ReceiptUpdateType: UpdateType {
    LIST_UPDATE, //RecyclerViewを全て再描画する
    EDIT_CLEAR, //editText1をクリアする
    SPINNER_AS_ACCOUNT, //spinner1に口座一覧をセットする
    TEXT_AS_TOTAL //textView1に合計金額を反映する
}