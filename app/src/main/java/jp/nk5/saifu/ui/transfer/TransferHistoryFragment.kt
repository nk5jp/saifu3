package jp.nk5.saifu.ui.transfer

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentTransferHistoryBinding
import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.service.TransferHistoryService
import jp.nk5.saifu.ui.util.TransferListAdapter
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.transfer.TransferHistoryUpdateType
import jp.nk5.saifu.viewmodel.transfer.TransferHistoryViewModel
import jp.nk5.saifu.viewmodel.UpdateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransferHistoryFragment : MyFragment(), View.OnClickListener, Observer {

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentTransferHistoryBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { TransferHistoryViewModel() } //本画面のviewModel
    private val service by lazy { TransferHistoryService(common.transferRepository, viewModel) }
    private val button by lazy { binding.button1 }  //検索年月検索用のボタン
    private val recyclerView by lazy { binding.recyclerView1 } //振替履歴一覧用のrecyclerView

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
            _binding = FragmentTransferHistoryBinding.inflate(
                inflater, container, false
            )
            button.setOnClickListener(this)
            recyclerView.adapter = TransferListAdapter(viewModel.transfers)
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }
        // Inflate the layout for this fragment
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
            service.updateView(MyDate.today())
        }
    }

    /**
     * 画面の更新処理、各viewModelから通知される
     */
    @SuppressLint("NotifyDataSetChanged")
    override suspend fun updateView(updateTypes: List<UpdateType>) {
        withContext(Dispatchers.Main) {
            for (updateType in updateTypes) {
                when (updateType) {
                    TransferHistoryUpdateType.LIST_UPDATE -> {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    TransferHistoryUpdateType.BUTTON_AS_DATE -> {
                        button.text = getString(R.string.btn_ym).format(
                            viewModel.date.year,
                            viewModel.date.month
                        )
                    }
                }
            }
        }
    }

    /**
     * 検索対象の年月を指定して検索処理を実行する。
     * Androidに年月指定用のモジュールが存在しないので、カレンダーで指定する方式を採用する。
     * View.OnClickListenerで定義されている関数の実装
     */
    override fun onClick(view: View) {
        DatePickerDialog(
            requireActivity(),
            { _, year, month, day ->
                CoroutineScope(Dispatchers.Main).launch {
                    //1月は0月となっているので、サービスに渡す月は+1しておく
                    service.updateView(MyDate(year, month + 1, day))
                }
            },
            viewModel.date.year, //ダイアログの初期年
            viewModel.date.month - 1, //ダイアログの初期月、ここは逆にマイナス1が必要
            viewModel.date.day).show() //ダイアログの初期日
    }

}