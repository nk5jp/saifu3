package jp.nk5.saifu.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentAccountBinding
import jp.nk5.saifu.service.AccountService
import jp.nk5.saifu.ui.util.AccountListAdapter
import jp.nk5.saifu.viewmodel.account.AccountUpdateType
import jp.nk5.saifu.viewmodel.account.AccountViewModel
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.UpdateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment
    : MyFragment(), View.OnClickListener, Observer, AccountListAdapter.OnItemClickListener
{

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { AccountViewModel() } //本画面のviewModel
    private val service by lazy { AccountService(common.accountRepository, viewModel) } //サービス
    private val editText by lazy { binding.editText1 } //口座名の入力用editText
    private val recyclerView by lazy { binding.recyclerView1 } //口座一覧のrecyclerView
    private val button by lazy { binding.button1 } //開設or編集ボタン

    /**
     * viewModelの監視対象にこのフラグメントを追加する
     * この処理はインスタンスが生成された初回のみ実行される
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addObserver(this)
    }

    /**
     * 初回表示の際は、レイアウトをインフレートし、アダプター情報を設定する
     * Backボタンによる再表示の場合はレイアウト情報をそのまま返却する
     * ちなみにこの処理はホームボタンやタスクボタンを介しての再表示する際には通過しない
     * （その必要がある処理はonStartに記述すること）
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            //以下の一連の処理はインスタンスを初期生成したときのみ通過する
            _binding = FragmentAccountBinding.inflate(inflater, container, false)
            button.setOnClickListener(this)
            //この時点ではviewModelの参照先メモリを共有しているだけで、その先のListは空
            recyclerView.adapter = AccountListAdapter(
                viewModel.accounts,
                this,
                viewModel.selectedPositions
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }
        return binding.root
    }

    /**
     * viewModel内のデータ初期化と描画を行う
     * この処理は必ずonCreateViewの後に来るため、順序性管理の必要がある処理はこちらに記載する
     * なお、こちらの通過ルールもonCreateViewに順ずる
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            service.updateView()
        }
    }


    /**
     * 画面の更新処理、各viewModelから通知される
     */
    @SuppressLint("NotifyDataSetChanged")
    override suspend fun updateView(updateTypes: List<UpdateType>) {
        withContext(Dispatchers.Main) {
            for(updateType in updateTypes) {
                when(updateType) {
                    //RecyclerViewに更新を通知する
                    AccountUpdateType.LIST_UPDATE -> {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    //editTextを空にする
                    AccountUpdateType.EDIT_CLEAR -> {
                        editText.setText("")
                    }
                    //editTextに選択している口座名を入力する
                    AccountUpdateType.EDIT_INPUT -> {
                        editText.setText(viewModel.getSelectingAccount().name)
                    }
                    //buttonの文字列を「開設」にする
                    AccountUpdateType.BUTTON_AS_CREATE -> {
                        button.setText(R.string.btn_account)
                    }
                    //buttonの文字列を「変更」にする
                    AccountUpdateType.BUTTON_AS_UPDATE -> {
                        button.setText(R.string.btn_update)
                    }
                }
            }
        }
    }

    /**
     * 開設or更新ボタンを押下したときの処理
     * View.OnClickListenerで定義されている関数の実装
     */
    override fun onClick(view: View) {
        try {
            val name = editText.text.toString()
            //エラーチェック：空白は受け付けない
            if (name == "") { alert("口座名が未入力です"); return }
            CoroutineScope(Dispatchers.Main).launch {
                service.updateAccount(name)
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * 選択した行番号を踏まえ、recyclerView上の選択／非選択のモードを更新する
     * AccountListAdapter.OnItemClickListenerで定義されている関数の実装
     */
    override fun onItemClick(view: View) {
        try {
            val newPosition = recyclerView.getChildAdapterPosition(view)
            CoroutineScope(Dispatchers.Main).launch {
                service.selectAccount(newPosition)
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * 長押しした行の口座を削除して良いか伺うダイアログを表示する
     * AccountListAdapter.OnItemClickListenerで定義されている関数の実装
     */
    override fun onItemLongClick(view: View): Boolean {
        val position = recyclerView.getChildAdapterPosition(view)
        AlertDialog.Builder(requireContext())
            .setTitle("%sを削除しますか？".format(
                viewModel.getAccountByPosition(position).name
            ))
            .setMessage("削除後は元に戻せません")
            .setPositiveButton("YES") { _, _ ->
                //YES押下時：対象口座を削除する
                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        service.deleteAccount(position)
                    }
                } catch (e: Exception) {
                    alert(e.toString())
                }
            }
            .setNegativeButton("NO") { _, _ ->
                //NO押下時：何も実行しない
            }.show()
        return true
    }
}