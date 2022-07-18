package jp.nk5.saifu.domain

enum class Title(val id: Int, val text: String) {
    FOOD(1,"食費"),
    EQUIP(2,"設備費"),
    SELF(3,"自己啓発費"),
    ENTERTAINMENT(4, "娯楽費"),
    UTIL(5, "光熱費"),
    EXP(6,"経費"),
    TRAVEL(7, "旅費"),
    MISC(8, "雑費")
}