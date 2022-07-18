package jp.nk5.saifu.domain

/**
 * 費用科目を意味するドメイン
 * 経験則的に科目のカスタマイズ性を感じないので永続化はしていない
 * 将来的な追加を踏まえつつ、雑費は最後にしたいので99としている
 */
enum class Title(val id: Int, val text: String) {
    FOOD(1,"食費"),
    EQUIP(2,"設備費"),
    SELF(3,"自己啓発費"),
    ENTERTAINMENT(4, "娯楽費"),
    UTIL(5, "光熱費"),
    EXP(6,"経費"),
    TRAVEL(7, "旅費"),
    MISC(99, "雑費")
}