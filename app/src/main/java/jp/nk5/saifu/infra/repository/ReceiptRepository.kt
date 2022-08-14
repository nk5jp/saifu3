package jp.nk5.saifu.infra.repository

import jp.nk5.saifu.domain.*
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.infra.AppDatabase
import jp.nk5.saifu.infra.entity.EntityReceipt
import jp.nk5.saifu.infra.entity.EntityReceiptDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReceiptRepository(
    private val db: AppDatabase,
    private val accountRepository: AccountRepository
): jp.nk5.saifu.domain.repository.ReceiptRepository {


    /**
     * レシートを作成もしくは更新する処理
     * CreateかUpdateかは、idが0か否かで判定する（Roomの仕様にも基づく）
     * detailsテーブルへの処理はCreateでもUpdateでもinsertとなる
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     */
    override suspend fun setReceipt(receipt: Receipt) = withContext(Dispatchers.IO) {
        //receiptsテーブルへの処理。
        if (receipt.id == 0) {
            //新規の場合
            val id: Int = db.receiptDao().insertReceipt(
                EntityReceipt(
                    0,
                    receipt.date.getYmd(),
                    receipt.account.id
                )
            ).toInt()
            receipt.id = id
        } else {
            //更新の場合
            db.receiptDao().updateReceipt(
                EntityReceipt(
                    receipt.id,
                    receipt.date.getYmd(),
                    receipt.account.id
                )
            )
            //更新前後でインデックスの同一性を保障できないのでdetailsは全て入れ替える必要がある
            db.receiptDao().deleteReceiptDetailsById(receipt.id)
        }
        //detailsテーブルへの処理。detail_idにはリストのインデックスを採用する
        for ((detailId, detail) in receipt.details.withIndex()) {
            db.receiptDao().insertReceiptDetail(
                EntityReceiptDetail(
                    receipt.id,
                    detailId,
                    detail.title.id,
                    detail.taxType.id,
                    detail.amount
                )
            )
        }
    }

    /**
     * 指定した年月日のレシートのリストを取得する処理
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     */
    override suspend fun getReceiptByYmd(date: MyDate)
    : MutableList<Receipt> = withContext(Dispatchers.IO) {
        //DBから該当レコード群をMapとして取得し、ドメインに変換していく
        db.receiptDao().selectByYmd(date.getYmd()).map { e ->
            //e.keyがEntityReceipt、e.valueがList<EntityReceiptDetails>
            val receiptDetails = mutableListOf<ReceiptDetail>()
            //detailsの変換
            for (detail in e.value) {
                receiptDetails.add(
                    ReceiptDetail(
                        detail.detailId,
                        Title.getTitleById(detail.titleId),
                        detail.amount,
                        TaxType.getTaxTypeById(detail.taxTypeId)
                    )
                )
            }
            //receiptの変換
            Receipt(
                e.key.id,
                MyDate(e.key.date),
                accountRepository.getAccountById(e.key.accountId),
                receiptDetails
            )
        }.toMutableList()
    }

    /**
     * 指定した期間内に作成されたレシートのリストを取得する処理
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     */
    override suspend fun getReceiptByDuration(fromDate: MyDate, toDate: MyDate)
            : MutableList<Receipt> = withContext(Dispatchers.IO) {
        //DBから該当レコード群をMapとして取得し、ドメインに変換していく
        db.receiptDao().selectByDuration(fromDate.getYmd(), toDate.getYmd()).map { e ->
            //e.keyがEntityReceipt、e.valueがList<EntityReceiptDetails>
            val receiptDetails = mutableListOf<ReceiptDetail>()
            //detailsの変換
            for (detail in e.value) {
                receiptDetails.add(
                    ReceiptDetail(
                        detail.detailId,
                        Title.getTitleById(detail.titleId),
                        detail.amount,
                        TaxType.getTaxTypeById(detail.taxTypeId)
                    )
                )
            }
            //receiptの変換
            Receipt(
                e.key.id,
                MyDate(e.key.date),
                accountRepository.getAccountById(e.key.accountId),
                receiptDetails
            )
        }.toMutableList()
    }

    /**
     * 指定したIDのレシートのインスタンスを取得する処理
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     */
    override suspend fun getReceiptById(id: Int)
            : Receipt = withContext(Dispatchers.IO) {
        //DBから該当レコード群をMapとして取得し、ドメインに変換していく
        db.receiptDao().selectById(id).map { e ->
            //e.keyがEntityReceipt、e.valueがList<EntityReceiptDetails>
            val receiptDetails = mutableListOf<ReceiptDetail>()
            //detailsの変換
            for (detail in e.value) {
                receiptDetails.add(
                    ReceiptDetail(
                        detail.detailId,
                        Title.getTitleById(detail.titleId),
                        detail.amount,
                        TaxType.getTaxTypeById(detail.taxTypeId)
                    )
                )
            }
            //receiptの変換
            Receipt(
                e.key.id,
                MyDate(e.key.date),
                accountRepository.getAccountById(e.key.accountId),
                receiptDetails
            )
        }.toMutableList()[0]
    }

    /**
     * レシートの物理削除処理
     * 紐づくdetailsはカスケードで削除されるので特に処理は記述しない
     * IO処理を含むためsuspend functionとして宣言している。コルーチン内で宣言すること。
     */
    override suspend fun deleteReceipt(receipt: Receipt) = withContext(Dispatchers.IO) {
        db.receiptDao().deleteReceipt(
            EntityReceipt(
                receipt.id,
                receipt.date.getYmd(),
                receipt.account.id
            )
        )
    }


}