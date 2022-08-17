package jp.nk5.saifu.domain

/**
 * トランザクションを意味するドメイン
 * TransactionではRoomのClass名などと重複するためこの名称としている
 * idはinfra側の都合で導入される要素。0の場合が永続化前、それ以外の場合は永続化後。
 * debitが借方、creditが貸方
 * amountには貸方から借方への流入資金量が表現される、キャンセルの可能性あがるので一応マイナスは許容
 */
open class Transfer(
    var id: Int,
    val date: MyDate,
    val debit: Account,
    val credit: Account?,
    val amount: Int
    )