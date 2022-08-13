package jp.nk5.saifu.domain

/**
 * レシートの明細を意味するドメイン
 * 科目＆税種別のenumと金額が保持されている
 */
data class ReceiptDetail(var id: Int, var title: Title, var amount: Int, var taxType: TaxType)
