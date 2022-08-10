package jp.nk5.saifu.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.nk5.saifu.MyFragment
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentAccountBinding
import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.service.AccountService
import jp.nk5.saifu.ui.util.AccountListAdapter
import jp.nk5.saifu.viewmodel.AccountUpdateType
import jp.nk5.saifu.viewmodel.AccountViewModel
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
    private val binding get() = _binding!!
    private val viewModel by lazy { AccountViewModel() }
    private val service by lazy { AccountService(common.accountRepository, viewModel) } //サービス
    private var _editText: EditText? = null
    private val editText get() = _editText!!
    private var _recyclerView: RecyclerView? = null
    private val recyclerView get() = _recyclerView!!
    private var _button: Button? = null
    private val button get() = _button!!

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
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        _editText = binding.editText1
        _recyclerView = binding.recyclerView1
        _button = binding.button1
        button.setOnClickListener(this)
        recyclerView.adapter = AccountListAdapter(
            viewModel.accounts,
            this,
            viewModel.selectedPositions
        )
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            service.initializeView()
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
                        editText.setText(viewModel.getSelectedAccount().name)
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
     * ボタンを押下したときの処理
     */
    override fun onClick(view: View) {
        try {
            val name = editText.text.toString()
            //エラーチェック：空白は受け付けない
            if (name == "") { alert("口座名が未入力です"); return }
            CoroutineScope(Dispatchers.Main).launch {
                if (viewModel.isSelected()) {
                    //選択している場合：更新処理
                    service.updateAccount(viewModel.getSelectingPosition(), name)
                } else {
                    //選択していない場合：開設処理
                    service.createAccount(name)
                }
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * recyclerViewの各行を選択したときの処理
     */
    override fun onItemClick(view: View) {
        try {
            val newPosition = recyclerView.getChildAdapterPosition(view)
            if (viewModel.isSelected() && viewModel.getSelectingPosition() == newPosition) {
                //選択済みの行を再選択した場合：選択解除処理
                CoroutineScope(Dispatchers.Main).launch {
                    service.unselectAccount(newPosition)
                }
            } else {
                //それ以外の場合：選択処理
                CoroutineScope(Dispatchers.Main).launch {
                    service.selectAccount(newPosition)
                }
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * recyclerViewの各行を長押ししたときの処理
     */
    override fun onItemLongClick(view: View): Boolean {
        val position = recyclerView.getChildAdapterPosition(view)
        AlertDialog.Builder(requireContext())
            .setTitle("%sを削除しますか？".format(viewModel.getAccountByPosition(position).name))
            .setMessage("削除後は元に戻せません")
            .setPositiveButton("YES") { _, _ ->
                //対象口座を削除する
                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        service.deleteAccount(position)
                    }
                } catch (e: Exception) {
                    alert(e.toString())
                }
            }
            .setNegativeButton("NO") { _, _ ->
                //何も実行しない
            }.show()
        return true
    }
}