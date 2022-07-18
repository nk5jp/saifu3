package jp.nk5.saifu.domain

/**
 * トランザクションを意味するドメイン
 * TransactionではRoomのClass名などと重複するためこの名称としている
 * debitが借方、creditが貸方
 * amountには貸方から借方への流入資金量が表現される、キャンセルの可能性あがるので一応マイナスは許容
 * 実際にはこれを継承した子クラスが扱われる */
open class Transfer(val date: MyDate, val debit: Account, val credit: Account?, val amount: Int)