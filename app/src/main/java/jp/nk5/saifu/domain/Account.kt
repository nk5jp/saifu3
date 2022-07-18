package jp.nk5.saifu.domain

/**
 * 口座情報を意味するドメイン。
 * isValidは論理削除用のフラグ。これがonの場合、リストアップ対象から永続的に除外される。
 */
open class Account(open var name: String, open var amount: Int, open var isValid: Boolean)
