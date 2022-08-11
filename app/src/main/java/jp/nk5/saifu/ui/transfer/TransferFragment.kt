package jp.nk5.saifu.ui.transfer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.nk5.saifu.MyFragment
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentTransferBinding
import jp.nk5.saifu.service.TransferService
import jp.nk5.saifu.ui.util.AccountListAdapter
import jp.nk5.saifu.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransferFragment
    : MyFragment(), Observer, AccountListAdapter.OnItemClickListener {


    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { TransferViewModel() } //本画面のviewModel
    private val service by lazy { TransferService(common.accountRepository, viewModel) }
    private val recyclerView by lazy { binding.recyclerView1 } //口座一覧のrecyclerView

    /**
     * navControllerなどによる遷移でインスタンスが生成された初回のみに実行される処理
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addObserver(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Backボタン等で遷移した場合は元々存在するインスタンスをそのまま返却する
        if (_binding == null) {
            _binding = FragmentTransferBinding.inflate(inflater, container, false)
            recyclerView.adapter = AccountListAdapter(
                viewModel.accounts,
                this,
                viewModel.selectedPositions
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
            binding.button3.setOnClickListener{
                findNavController().navigate(R.id.action_transferFragment_to_accountFragment)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            service.initializeView()
        }
    }

    override fun onItemClick(view: View) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(view: View): Boolean {
        TODO("Not yet implemented")
    }

    @SuppressLint("NotifyDataSetChanged")
    override suspend fun updateView(updateTypes: List<UpdateType>) {
        withContext(Dispatchers.Main) {
            for(updateType in updateTypes) {
                when(updateType) {
                    //RecyclerViewに更新を通知する
                    TransferUpdateType.LIST_UPDATE -> {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}