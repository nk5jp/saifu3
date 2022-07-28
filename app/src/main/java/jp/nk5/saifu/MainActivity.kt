package jp.nk5.saifu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.repository.AccountRepository
import jp.nk5.saifu.infra.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "saifu3-db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch(Dispatchers.IO) {
            val repository: AccountRepository = jp.nk5.saifu.infra.repository.AccountRepository(db)
            var lists = repository.getAccounts()
            print(lists)
        }
    }

}