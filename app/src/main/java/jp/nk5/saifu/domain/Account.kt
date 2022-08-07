package jp.nk5.saifu.domain

/**
 * 口座情報を意味するドメイン。
 * idはinfra側の都合で導入される要素。0の場合が永続化前、それ以外の場合は永続化後。
 * isValidは論理削除用のフラグ。これがonの場合、リストアップ対象から永続的に除外される。
 */
class Account(var id: Int, var name: String, var amount: Int, var isValid: Boolean) {
    // 初期実装の場合は名前以外のパラメータが自明であるため、そちら用のコンストラクタも用意しておく
    constructor(name: String) : this(0, name, 0, true)
}
