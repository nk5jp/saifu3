package jp.nk5.saifu.domain

data class Receipt(val date: MyDate, var account: Account, var amount: Int)
