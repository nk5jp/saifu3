package jp.nk5.saifu.ui

import android.widget.Toast
import androidx.fragment.app.Fragment
import jp.nk5.saifu.Common

open class MyFragment: Fragment() {

    //各フラグメントのonCreateViewで初期化
    val common by lazy {context?.applicationContext as Common }

    /**
     * 警告処理
     */
    fun alert(string: String)
    {
        Toast.makeText(activity, string, Toast.LENGTH_SHORT).show()
    }
}