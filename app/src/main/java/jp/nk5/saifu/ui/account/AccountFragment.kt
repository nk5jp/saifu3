package jp.nk5.saifu.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.Common
import jp.nk5.saifu.MyFragment
import jp.nk5.saifu.databinding.FragmentAccountBinding
import jp.nk5.saifu.service.AccountService
import jp.nk5.saifu.ui.util.AccountListAdapter
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.UpdateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountFragment
    : MyFragment(), View.OnClickListener, Observer, AccountListAdapter.OnItemClickListener
{

    /**
     * 処理で使用するパラメータ群
     */
    private val viewModel by lazy { common.accountViewModel } //ビューモデル
    private val service by lazy { AccountService(common.accountRepository, viewModel) } //サービス
    private val editText by lazy { binding.editText1 } //名称入力用のeditText
    private val recyclerView by lazy { binding.recyclerView1 } //口座リストのrecyclerView

    /**
     * viewModelの監視対象にこのフラグメントを追加する
     * lazyによりviewModelの初期化も行われる
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addObserver(this)
    }

    /**
     * bindingおよびview情報を初期化
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullableBinding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.button1.setOnClickListener(this)
        recyclerView.adapter = AccountListAdapter(
            viewModel.accounts,
            this,
            viewModel.selectedPosition
        )
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    /**
     * 画面の更新処理、各viewModelから通知される
     */
    override fun updateView(updateTypes: List<UpdateType>) {
        for(updateType in updateTypes) {
            print(updateType)
        }
    }

    /**
     * ボタンを押下したときの処理
     */
    override fun onClick(view: View) {
        try {
            val name = editText.text.toString()
            //エラーチェック：空白は受け付けない
            if (name == "") { alert("口座名が未入力です"); return }
            CoroutineScope(Dispatchers.Main).launch {
                service.createAccount(name)
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * recyclerViewの各行を選択したときの処理
     */
    override fun onItemClick(view: View) {
        alert(view.toString())
    }

    /**
     * recyclerViewの各行を長押ししたときの処理
     */
    override fun onItemLongClick(view: View): Boolean {
        alert(view.toString())
        return true
    }
}