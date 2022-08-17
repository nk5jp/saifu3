package jp.nk5.saifu.domain.repository

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Transfer

/**
 * 振替を管理するRepository
 */
interface TransferRepository {
    suspend fun setTransfer(transfer: Transfer)
    suspend fun getTransferByYearMonth(yearMonth: Int): List<Transfer>
    suspend fun getTransferByDuration(fromDate: MyDate, toDate: MyDate): List<Transfer>
}