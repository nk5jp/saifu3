package jp.nk5.saifu.domain

/**
 * 口座情報を意味するドメイン
 * 実際にはこれを継承した子クラスが扱われる
 */
open class Account(open var name: String, open var amount: Int, open var isValid: Boolean)
