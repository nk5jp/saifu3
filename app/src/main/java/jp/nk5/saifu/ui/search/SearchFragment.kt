package jp.nk5.saifu.ui.search

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentSearchBinding
import jp.nk5.saifu.domain.MyDate
import jp.nk5.saifu.service.SearchService
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.ui.util.TitleListAdapter
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.UpdateType
import jp.nk5.saifu.viewmodel.search.SearchUpdateType
import jp.nk5.saifu.viewmodel.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : MyFragment(), Observer, View.OnClickListener {

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { SearchViewModel() } //本画面のviewModel
    private val service by lazy { //本画面のservice
        SearchService(
            common.receiptRepository,
            common.transferRepository,
            viewModel
        )
    }
    private val button2 by lazy { binding.button2 } //検索対象の上限年月日を設定するためのbutton
    private val button3 by lazy { binding.button3 } //検索対象の下限年月日を設定するためのbutton
    private val recyclerView by lazy { binding.recyclerView1 } //科目ごとの金額を設定するrecyclerView

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
            _binding = FragmentSearchBinding.inflate(inflater, container, false)
            button2.setOnClickListener(this)
            button3.setOnClickListener(this)
            //この時点ではviewModelの参照先メモリを共有しているだけで、その先のListは空
            recyclerView.adapter = TitleListAdapter(
                viewModel.titles
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
            //ボタンクリック時の遷移処理を付加する
            binding.button1.setOnClickListener {
                //配列の1要素目は入力年月日、2要素目は対象ID（今回は新規なので0）
                findNavController().navigate(R.id.action_searchFragment_to_searchDetailFragment)
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
            service.updateView()
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
                        SearchUpdateType.LIST_UPDATE -> {
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                        //button3の文字列を「From：YYYY/MM/DD」にする
                        SearchUpdateType.BUTTON_AS_FROM -> {
                            button3.text = getString(R.string.btn_ymd_from).format(
                                viewModel.fromDate.year,
                                viewModel.fromDate.month,
                                viewModel.fromDate.day
                            )
                        }
                        //button2の文字列を「From：YYYY/MM/DD」にする
                        SearchUpdateType.BUTTON_AS_TO -> {
                            button2.text = getString(R.string.btn_ymd_to).format(
                                viewModel.toDate.year,
                                viewModel.toDate.month,
                                viewModel.toDate.day
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    override fun onClick(view: View) {
        try {
            when (view.id) {
                button2.id -> {
                    DatePickerDialog(
                        requireActivity(),
                        { _, year, month, day ->
                            CoroutineScope(Dispatchers.Main).launch {
                                //1月は0月となっているので、サービスに渡す月は+1しておく
                                service.updateToDate(MyDate(year, month + 1, day))
                            }
                        },
                        viewModel.toDate.year, //ダイアログの初期年
                        viewModel.toDate.month - 1, //ダイアログの初期月、ここは逆にマイナス1が必要
                        viewModel.toDate.day
                    ).show() //ダイアログの初期日
                }
                button3.id -> {
                    DatePickerDialog(
                        requireActivity(),
                        { _, year, month, day ->
                            CoroutineScope(Dispatchers.Main).launch {
                                //1月は0月となっているので、サービスに渡す月は+1しておく
                                service.updateFromDate(MyDate(year, month + 1, day))
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
}