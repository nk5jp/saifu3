package jp.nk5.saifu.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.appcompat.app.AlertDialog
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
    : MyFragment(), Observer,
    ReceiptListAdapter.OnItemClickListener, CalendarView.OnDateChangeListener
{

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { MainViewModel() } //本画面のviewModel
    private val service by lazy {
        MainService(
            common.receiptRepository,
            common.accountRepository,
            viewModel
        ) } //Service
    private val recyclerView by lazy { binding.recyclerView1 } //レシート一覧のrecyclerView
    private val calendarView by lazy { binding.calendarView1 } //カレンダービュー

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
            calendarView.setOnDateChangeListener(this)
            //この時点ではviewModelの参照先メモリを共有しているだけで、その先のListは空
            recyclerView.adapter = ReceiptListAdapter(
                viewModel.receipts,
                this
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
            //ボタンクリック時の遷移処理を付加する
            binding.button1.setOnClickListener {
                //配列の1要素目は入力年月日、2要素目は対象ID（今回は新規なので0）
                val action = MainFragmentDirections
                    .actionMainFragmentToReceiptFragment(viewModel.date.getYmd(), 0)
                findNavController().navigate(action)
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
            //recyclerViewを初期化する
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
                        MainUpdateType.LIST_UPDATE -> {
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
     * 選択した行番号に対応づくレシートの作成日とidを用いてレシート編集画面に遷移する
     * ReceiptListAdapter.OnItemClickListenerで定義されている関数の実装
     */
    override fun onItemClick(view: View) {
        try {
            val position = recyclerView.getChildAdapterPosition(view)
            val action = MainFragmentDirections
                .actionMainFragmentToReceiptFragment(
                    viewModel.date.getYmd(),
                    viewModel.receipts[position].id
                )
            findNavController().navigate(action)
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    /**
     * 長押しした行のレシートを削除して良いか伺うダイアログを表示する
     * ReceiptListAdapter.OnItemClickListenerで定義されている関数の実装
     */
    override fun onItemLongClick(view: View): Boolean {
        try {
            val position = recyclerView.getChildAdapterPosition(view)
            AlertDialog.Builder(requireContext())
                .setTitle("レシートを削除しますか？")
                .setMessage("削除後は元に戻せません")
                .setPositiveButton("YES") { _, _ ->
                    //YES押下時：対象レシートを削除する
                    try {
                        CoroutineScope(Dispatchers.Main).launch {
                            service.deleteReceipt(position)
                        }
                    } catch (e: Exception) {
                        alert(e.toString())
                    }
                }
                .setNegativeButton("NO") { _, _ ->
                    //NO押下時：何も実行しない
                }.show()
        } catch (e: Exception) {
            alert(e.toString())
        }
        return true
    }

    /**
     * 検索対象の年月日を指定して検索処理を実行する。
     * CalendarView.OnDateChangeListenerで定義されている関数の実装
     */
    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, day: Int) {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                //1月は0月となっているので、サービスに渡す月は+1しておく
                service.updateView(MyDate(year, month + 1, day))
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

}