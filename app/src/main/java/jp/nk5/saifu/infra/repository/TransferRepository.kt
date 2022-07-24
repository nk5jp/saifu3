package jp.nk5.saifu.infra.repository

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.domain.Transfer
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.infra.AppDatabase
import jp.nk5.saifu.infra.entity.EntityTransfer

class TransferRepository(
        private val db: AppDatabase,
        private val accountRepository: AccountRepository
    ) : jp.nk5.saifu.domain.repository.TransferRepository {


    /**
     * 新しい振替を保存する処理
     */
    override fun setTransfer(myDate: MyDate, debit: Account, credit: Account?, amount: Int) {
        db.transferDao().insertTransfer(
            EntityTransfer(
                0,
                myDate.getYmd(),
                debit.id,
                credit?.id,
                amount
            )
        )
    }

    /**
     * 指定した年月に該当するTransferのリストを返却する。
     * 用途を踏まえ、このリストは永続化しない。すなわち、2度コールした場合、論理的に同じ振替でも別のインスタンスとなる。
     * また、この処理は例外をスローし得る（getAccountById）点を留意すること
     */
    override fun getTransferByYearMonth(yearMonth: Int) : List<Transfer> {
        return db.transferDao().selectByYearMonth(yearMonth).map { e ->
            Transfer(
                e.id,
                MyDate(e.date),
                accountRepository.getAccountById(e.debitId),
                accountRepository.getAccountById(e.debitId),
                e.amount
            )
        }.toMutableList()
    }
}