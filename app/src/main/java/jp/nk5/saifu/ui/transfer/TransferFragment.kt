package jp.nk5.saifu.ui.transfer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentTransferBinding
import jp.nk5.saifu.service.TransferService
import jp.nk5.saifu.ui.util.AccountListAdapter
import jp.nk5.saifu.viewmodel.*
import jp.nk5.saifu.viewmodel.transfer.TransferUpdateType
import jp.nk5.saifu.viewmodel.transfer.TransferViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransferFragment
    : MyFragment(), View.OnClickListener, Observer, AccountListAdapter.OnItemClickListener {


    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { TransferViewModel() } //本画面のviewModel
    private val service by lazy {
        TransferService(common.accountRepository, common.transferRepository, viewModel)
    }
    private val recyclerView by lazy { binding.recyclerView1 } //口座一覧のrecyclerView
    private val editText by lazy { binding.editText1 } //金額入力用のeditText
    private val textView1 by lazy { binding.textView1 } //選択状態説明用のtextView
    private val textView2 by lazy { binding.textView2 } //合計金額表示用のtextView

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
        //Backボタン等で遷移した場合は元々存在するインスタンスをそのまま返却する
        if (_binding == null) {
            //以下の一連の処理はインスタンスを初期生成したときのみ通過する
            _binding = FragmentTransferBinding.inflate(inflater, container, false)
            binding.button1.setOnClickListener(this)
            //この時点ではviewModelの参照先メモリを共有しているだけで、その先のListは空
            recyclerView.adapter = AccountListAdapter(
                viewModel.accounts,
                this,
                viewModel.selectedPositions
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)

            //画面遷移のイベントをボタンに設定する
            binding.button2.setOnClickListener{
                findNavController().navigate(R.id.action_transferFragment_to_transferHistoryFragment)
            }
            binding.button3.setOnClickListener{
                findNavController().navigate(R.id.action_transferFragment_to_accountFragment)
            }
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
            service.initializeView()
        }
    }

    /**
     * 画面の更新処理、各viewModelから通知される
     */
    @SuppressLint("NotifyDataSetChanged")
    override suspend fun updateView(updateTypes: List<UpdateType>) {
        try {
            withContext(Dispatchers.Main) {
                for (updateType in updateTypes) {
                    when (updateType) {
                        //RecyclerViewに更新を通知する
                        TransferUpdateType.LIST_UPDATE -> {
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                        //editTextを空にする
                        TransferUpdateType.EDIT_CLEAR -> {
                            editText.setText("")
                        }
                        //textView1の文字列を「未選択」にする
                        TransferUpdateType.TEXTVIEW_AS_UNSELECTED -> {
                            textView1.setText(R.string.lbl_unselected)
                        }
                        //textView1の文字列を「XXXに入金」にする
                        TransferUpdateType.TEXTVIEW_AS_PAYMENT -> {
                            textView1.text = "%sに入金".format(
                                viewModel.getDebitAccount().name
                            )
                        }
                        //textView1の文字列を「XXXからYYYに振替」にする
                        TransferUpdateType.TEXTVIEW_AS_TRANSFER -> {
                            textView1.text = "%sから%sに振替".format(
                                viewModel.getCreditAccount().name,
                                viewModel.getDebitAccount().name
                            )
                        }
                        //textView2の文字列を「合計：XXX円」にする
                        TransferUpdateType.TEXTVIEW_AS_SUM -> {
                            textView2.text = "合計：%,d円".format(
                                service.sumAmounts()
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * 振替ボタンを押下したときの処理
     * View.OnClickListenerで定義されている関数の実装
     */
    override fun onClick(view: View) {
        try {
            //エラーチェック：口座未選択の場合は何もしない
            if (viewModel.isUnselected()) {alert("口座が選択されていません"); return }
            val strAmount = editText.text.toString()
            //エラーチェック：空白は受け付けない
            if (strAmount == "") { alert("金額が未入力です"); return }
            val amount = strAmount.toInt()
            CoroutineScope(Dispatchers.Main).launch {
                service.transfer(amount)
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
     * 何もしないが、アダプター共通化の都合上、空の処理を実装している
     * AccountListAdapter.OnItemClickListenerで定義されている関数の実装
     */
    override fun onItemLongClick(view: View): Boolean {
        return true
    }
}