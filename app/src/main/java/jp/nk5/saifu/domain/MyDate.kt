package jp.nk5.saifu.domain

import java.time.LocalDate

/**
 * 日付を意味するドメイン
 * 2種類のコンストラクタからインスタンス化できるようにする。
 */
class MyDate {
    val year: Int
    val month: Int
    val day: Int

    companion object {
        //呼び出したタイミングの当日日付のMyDateインスタンスを返却する
        fun today(): MyDate {
            val date = LocalDate.now()
            return MyDate(date.year, date.monthValue, date.dayOfMonth)
        }
    }

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

    /**
     * インスタンスの年月日をyyyymmdd形式のInt型で返却する
     */
    fun getYmd(): Int {
        return year * 10000 + month * 100 + day
    }

    /**
     * インスタンスの年月をyyyymm形式のInt型で返却する
     */
    fun getYm(): Int {
        return year * 100 + month
    }

}