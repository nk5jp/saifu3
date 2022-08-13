package jp.nk5.saifu.ui.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentMainBinding
import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.service.MainService
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.ui.util.ReceiptListAdapter
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.UpdateType
import jp.nk5.saifu.viewmodel.main.MainUpdateType
import jp.nk5.saifu.viewmodel.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment
    : MyFragment(), Observer, View.OnClickListener, ReceiptListAdapter.OnItemClickListener
{

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { MainViewModel() } //本画面のviewModel
    private val service by lazy { MainService(common.receiptRepository, viewModel) } //Service
    private val button by lazy { binding.button4 }  //検索用のボタン
    private val recyclerView by lazy { binding.recyclerView1 } //レシート一覧のrecyclerView

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
            _binding = FragmentMainBinding.inflate(inflater, container, false)
            button.setOnClickListener(this)
            //この時点ではviewModelの参照先メモリを共有しているだけで、その先のListは空
            recyclerView.adapter = ReceiptListAdapter(
                viewModel.receipts,
                this
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
            //ボタンクリック時の遷移処理を付加する
            binding.button1.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_receiptFragment)
            }
            binding.button2.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_transferFragment)
            }
            binding.button3.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
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
            service.updateView(MyDate.today())
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
                        MainUpdateType.LIST_UPDATE -> {
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                        //buttonの文字列を「YYYY/MM/DD」にする
                        MainUpdateType.BUTTON_AS_DATE -> {
                            button.text = getString(R.string.btn_ymd).format(
                                viewModel.date.year,
                                viewModel.date.month,
                                viewModel.date.day
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
     * 検索対象の年月日を指定して検索処理を実行する。
     * View.OnClickListenerで定義されている関数の実装
     */
    override fun onClick(view: View) {
        try {
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
                viewModel.date.day
            ).show() //ダイアログの初期日
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    override fun onItemClick(view: View) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(view: View): Boolean {
        TODO("Not yet implemented")
    }

}