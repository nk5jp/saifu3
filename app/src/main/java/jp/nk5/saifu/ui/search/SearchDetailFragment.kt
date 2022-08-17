package jp.nk5.saifu.ui.search

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentSearchDetailBinding
import jp.nk5.saifu.domain.Account
import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.service.SearchDetailService
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.ui.util.AccountSpinnerAdapter
import jp.nk5.saifu.ui.util.ReceiptForSearchListAdapter
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.UpdateType
import jp.nk5.saifu.viewmodel.search.SearchDetailUpdateType
import jp.nk5.saifu.viewmodel.search.SearchDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchDetailFragment
    : MyFragment(), Observer, View.OnClickListener, AdapterView.OnItemSelectedListener {

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentSearchDetailBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { SearchDetailViewModel() } //本画面のviewModel
    private val service by lazy { //本画面のservice
        SearchDetailService(
            common.accountRepository,
            common.receiptRepository,
            common.transferRepository,
            viewModel
        )
    }
    private val spinner by lazy { binding.spinner1 } //口座一覧用のspinner
    private val recyclerView by lazy { binding.recyclerView1 } //レシート一覧を表示するrecyclerView
    private val button1 by lazy { binding.button1 } //検索対象の上限年月日を設定するためのbutton
    private val button2 by lazy { binding.button2 } //検索対象の下限年月日を設定するためのbutton
    private val textView by lazy { binding.textView1 } //合計金額出力用のtextView

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
            _binding = FragmentSearchDetailBinding.inflate(inflater, container, false)
            button1.setOnClickListener(this)
            button2.setOnClickListener(this)
            spinner.onItemSelectedListener = this
            //この時点ではviewModelの参照先メモリを共有しているだけで、その先のListは空
            recyclerView.adapter = ReceiptForSearchListAdapter(
                viewModel.receipts
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
                        //Spinner1にデータをセットしリスナーを設定する
                        SearchDetailUpdateType.SPINNER_AS_ACCOUNT -> {
                            spinner.adapter = AccountSpinnerAdapter(
                                viewModel.accounts
                            )
                        }
                        //RecyclerViewに更新を通知する
                        SearchDetailUpdateType.LIST_UPDATE -> {
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                        //button2の文字列を「From：YYYY/MM/DD」にする
                        SearchDetailUpdateType.BUTTON_AS_FROM -> {
                            button2.text = getString(R.string.btn_ymd_from).format(
                                viewModel.fromDate.year,
                                viewModel.fromDate.month,
                                viewModel.fromDate.day
                            )
                        }
                        //button1の文字列を「From：YYYY/MM/DD」にする
                        SearchDetailUpdateType.BUTTON_AS_TO -> {
                            button1.text = getString(R.string.btn_ymd_to).format(
                                viewModel.toDate.year,
                                viewModel.toDate.month,
                                viewModel.toDate.day
                            )
                        }
                        //textViewに総支出と総収入を反映する
                        SearchDetailUpdateType.TEXT_AS_TOTAL -> {
                            textView.text = "支出：%,d円 収入：%,d円".format(
                                viewModel.loss, viewModel.profit
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
     * viewのidでどちらのボタンが押下されたかを判断し、日付指定用のダイアログを出力する
     * View.OnClickListenerの実装
     */
    override fun onClick(view: View) {
        try {
            val account = spinner.selectedItem as Account
            when (view.id) {
                button1.id -> {
                    DatePickerDialog(
                        requireActivity(),
                        { _, year, month, day ->
                            CoroutineScope(Dispatchers.Main).launch {
                                //1月は0月となっているので、サービスに渡す月は+1しておく
                                service.updateToDate(
                                    MyDate(year, month + 1, day),
                                    account
                                )
                            }
                        },
                        viewModel.toDate.year, //ダイアログの初期年
                        viewModel.toDate.month - 1, //ダイアログの初期月、ここは逆にマイナス1が必要
                        viewModel.toDate.day
                    ).show() //ダイアログの初期日
                }
                button2.id -> {
                    DatePickerDialog(
                        requireActivity(),
                        { _, year, month, day ->
                            CoroutineScope(Dispatchers.Main).launch {
                                //1月は0月となっているので、サービスに渡す月は+1しておく
                                service.updateFromDate(
                                    MyDate(year, month + 1, day),
                                    account
                                )
                            }
                        },
                        viewModel.fromDate.year, //ダイアログの初期年
                        viewModel.fromDate.month - 1, //ダイアログの初期月、ここは逆にマイナス1が必要
                        viewModel.fromDate.day
                    ).show() //ダイアログの初期日
                }
                else -> {
                    throw Exception("想定しないidです")
                }
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * viewのidでどちらのボタンが押下されたかを判断し、日付指定用のダイアログを出力する
     * View.OnClickListenerの実装
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val account = spinner.selectedItem as Account
        CoroutineScope(Dispatchers.Main).launch {
            service.updateView(account)
        }
    }

    /**
     * アダプターがemptyになったときに選ばれる処理。本Fragmentでは通り得ないが実装が必要。
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }
}