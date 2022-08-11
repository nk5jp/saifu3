package jp.nk5.saifu.viewmodel

enum class TransferUpdateType: UpdateType {
    LIST_UPDATE,  //RecyclerViewを全て再描画する
    EDIT_CLEAR, //editText1をクリアする
    TEXTVIEW_AS_UNSELECTED, //textView1の文字列を「未選択」にする
    TEXTVIEW_AS_PAYMENT, //textView1の文字列を「XXXに入金」にする
    TEXTVIEW_AS_TRANSFER //textView1の文字列を「XXXからYYYに振替」にする
}