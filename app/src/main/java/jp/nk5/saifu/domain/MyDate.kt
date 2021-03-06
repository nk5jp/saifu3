package jp.nk5.saifu.domain

/**
 * 日付を意味するドメイン
 * 2種類のコンストラクタからインスタンス化できるようにする。
 */
class MyDate {
    val year: Int
    val month: Int
    val day: Int

    constructor(_ymd: Int) {
        year = _ymd / 10000
        month = (_ymd % 10000) / 100
        day = _ymd % 100
    }

    constructor(_year: Int, _month: Int, _day: Int) {
        year = _year
        month = _month
        day = _day
    }

    fun getYmd(): Int {
        return year * 10000 + month * 100 + day
    }

    fun getYm(): Int {
        return year * 100 + month
    }

}