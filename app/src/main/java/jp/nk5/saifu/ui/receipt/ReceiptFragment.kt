package jp.nk5.saifu.ui.receipt

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.databinding.FragmentReceiptBinding
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
    : MyFragment(), Observer, DetailListAdapter.OnItemClickListener {

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { ReceiptViewModel() } //本画面のviewModel
    private val service by lazy {
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
            for (updateType in updateTypes) {
                when (updateType) {
                    //spinnerに口座情報の一覧をセットする
                    ReceiptUpdateType.SPINNER_AS_ACCOUNT -> {
                        spinner2.adapter = AccountSpinnerAdapter(
                            viewModel.accounts
                        )
                    }

                    //RecyclerViewに更新を通知する
                    ReceiptUpdateType.LIST_UPDATE -> {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onItemClick(view: View) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(view: View): Boolean {
        TODO("Not yet implemented")
    }

}