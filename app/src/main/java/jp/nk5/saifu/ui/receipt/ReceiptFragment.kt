package jp.nk5.saifu.ui.receipt

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.R
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.databinding.FragmentReceiptBinding
import jp.nk5.saifu.domain.Title
import jp.nk5.saifu.service.ReceiptService
import jp.nk5.saifu.ui.util.AccountListAdapter
import jp.nk5.saifu.ui.util.AccountSpinnerAdapter
import jp.nk5.saifu.ui.util.DetailListAdapter
import jp.nk5.saifu.ui.util.TitleSpinnerAdapter
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.UpdateType
import jp.nk5.saifu.viewmodel.receipt.ReceiptUpdateType
import jp.nk5.saifu.viewmodel.receipt.ReceiptViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReceiptFragment
    : MyFragment(), View.OnClickListener, Observer, DetailListAdapter.OnItemClickListener {

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { ReceiptViewModel() } //本画面のviewModel
    private val service by lazy { //本画面のservice
        ReceiptService(
            common.accountRepository,
            common.receiptRepository,
            viewModel
        )
    }
    private val button1 by lazy { binding.button1 }  //購入もしくは修正用のボタン
    private val button2 by lazy { binding.button2 }  //明細追加用のボタン
    private val editText by lazy { binding.editText1 }  //金額入力用のボタン
    private val spinner2 by lazy { binding.spinner2 } //口座選択用のスピナー
    private val spinner1 by lazy { binding.spinner1 } //口座選択用のスピナー
    private val recyclerView by lazy { binding.recyclerView1 } //明細一覧

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
     * ちなみにこの処理はホームボタンやタスクボタンを介しての再表示する際には通過しない
     * （その必要がある処理はonStartに記述すること）
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentReceiptBinding.inflate(inflater, container, false)
            //各ボタンに押下時のイベントを設定
            button1.setOnClickListener(this)
            button2.setOnClickListener(this)
            //費用科目はDBではなくenumから作成し、かつ静的なリストであるため、ここで作成したら以後は再設定不要
            spinner1.adapter = TitleSpinnerAdapter()
            //この時点ではviewModelの参照先メモリを共有しているだけで、その先のListは空
            recyclerView.adapter = DetailListAdapter(
                viewModel.details,
                this
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
                        //spinner2に口座情報の一覧をセットする
                        ReceiptUpdateType.SPINNER_AS_ACCOUNT -> {
                            spinner2.adapter = AccountSpinnerAdapter(
                                viewModel.accounts
                            )
                        }
                        //editText1を空白にする
                        ReceiptUpdateType.EDIT_CLEAR -> {
                            editText.setText("")
                        }
                        //RecyclerView1に更新を通知する
                        ReceiptUpdateType.LIST_UPDATE -> {
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * 追加ボタンもしくは購入ボタンを押下したときの処理
     * 画面上にボタンが複数存在するので、場合分けで処理を選別する
     * View.OnClickListenerで定義されている関数の実装
     */
    override fun onClick(view: View) {
        try {
            when (view.id) {
                //購入（修正）ボタン押下時
                R.id.button1 -> {

                }
                //追加ボタン押下時
                R.id.button2 -> {
                    //spinner1から選択している費用科目を取得する
                    val title = spinner1.selectedItem as Title
                    val strAmount = editText.text.toString()
                    //エラーチェック：空白は受け付けない
                    if (strAmount == "") { alert("金額が未入力です"); return }
                    val amount = strAmount.toInt()
                    CoroutineScope(Dispatchers.Main).launch {
                        service.addDetail(title, amount)
                    }
                }
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * 選択した明細の税種別を変更する
     * 内税⇒外税8%⇒外税10%⇒内税の順番に変更する
     */
    override fun onItemClick(view: View) {
        try {
            val position = recyclerView.getChildAdapterPosition(view)
            CoroutineScope(Dispatchers.Main).launch {
                service.changeTaxType(position)
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    override fun onItemLongClick(view: View): Boolean {
        TODO("Not yet implemented")
    }
}