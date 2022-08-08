package jp.nk5.saifu.viewmodel

open class MyViewModel {
    private val observers: MutableList<Observer> = mutableListOf()

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    suspend fun notifyObservers(updateTypes: List<UpdateType>) {
        for(observer in observers) {
            observer.updateView(updateTypes)
        }
    }
}