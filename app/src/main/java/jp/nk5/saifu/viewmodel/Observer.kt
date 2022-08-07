package jp.nk5.saifu.viewmodel

interface Observer {
    fun updateView(updateTypes: List<UpdateType>)
}