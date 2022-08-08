package jp.nk5.saifu.viewmodel

interface Observer {
    suspend fun updateView(updateTypes: List<UpdateType>)
}