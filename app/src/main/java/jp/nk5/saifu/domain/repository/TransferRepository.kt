package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Transfer

/**
 * Transferを管理するRepository
 */
interface TransferRepository {
    fun setTransfer(myDate: MyDate, debit: Account, credit: Account?, amount: Int)
    fun getTransferByYearMonth(yearMonth: Int): List<Transfer>
}