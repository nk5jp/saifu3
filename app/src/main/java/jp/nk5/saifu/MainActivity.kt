package jp.nk5.saifu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.nk5.saifu.infra.repository.AccountRepository
import jp.nk5.saifu.viewmodel.AccountViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * common系の初期化のうち、Mainスレッド以外の処理が必要なものはこちらに記載する。
         */
        CoroutineScope(Dispatchers.IO).launch {
            val common = applicationContext as Common
            common.nullableAccountViewModel = AccountViewModel(common.accountRepository.getAccounts())
        }
    }
}