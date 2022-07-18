package jp.nk5.saifu.domain

/**
 * レシートの明細を意味するドメイン
 * 科目のenumと金額が保持されている
 */
data class ReceiptDetail(var title: Title, var amount: Int)
