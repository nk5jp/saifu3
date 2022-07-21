package jp.nk5.saifu.infra.mapper

import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.infra.entity.EntityAccount

/**
 * DomainとEntityを仲介するためのクラス
 * Repositoryはこちらをコレクションする形で実装する
 */
class MapperAccount (val id: Int, val account: Account) {

    fun getDomain(): Account {
        return account
    }

    fun getEntity(): EntityAccount {
        return EntityAccount(id, account.name, account.amount, account.isValid)
    }

}