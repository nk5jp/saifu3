package jp.nk5.saifu.infra.repository

import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Transfer
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.infra.AppDatabase
import jp.nk5.saifu.infra.entity.EntityTransfer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransferRepository(
        private val db: AppDatabase,
        private val accountRepository: AccountRepository
    ) : jp.nk5.saifu.domain.repository.TransferRepository {


    /**
     * 新しい振替を保存する処理
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     */
    override suspend fun setTransfer(transfer: Transfer): Unit = withContext(Dispatchers.IO) {
        db.transferDao().insertTransfer(
            EntityTransfer(
                0,
                transfer.date.getYmd(),
                transfer.debit.id,
                transfer.credit?.id,
                transfer.amount
            )
        )
    }

    /**
     * 指定した年月に該当するTransferのリストを返却する。
     * 用途を踏まえ、このリストは永続化しない。すなわち、2度コールした場合、論理的に同じ振替でも別のインスタンスとなる。
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     * また、この処理は例外をスローし得る（getAccountById）点を留意すること
     */
    override suspend fun getTransferByYearMonth(yearMonth: Int)
    : List<Transfer> = withContext(Dispatchers.IO) {
        db.transferDao().selectByYearMonth(yearMonth).map { e ->
            if (e.creditId == null) {
                Transfer(
                    e.id,
                    MyDate(e.date),
                    accountRepository.getAccountById(e.debitId),
                    null,
                    e.amount
                )
            } else {
                Transfer(
                    e.id,
                    MyDate(e.date),
                    accountRepository.getAccountById(e.debitId),
                    accountRepository.getAccountById(e.creditId),
                    e.amount
                )
            }
        }.toMutableList()
    }
}