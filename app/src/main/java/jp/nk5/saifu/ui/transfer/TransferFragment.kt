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
    private val binding get() = _binding!!
    private val viewModel by lazy { TransferViewModel() }
    private var _recyclerView: RecyclerView? = null
    private val recyclerView get() = _recyclerView!!
    private val service by lazy { TransferService(common.accountRepository, viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addObserver(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        _recyclerView = binding.recyclerView1
        recyclerView.adapter = AccountListAdapter(
            viewModel.accounts,
            this,
            viewModel.selectedPositions
        )
        recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.button3.setOnClickListener{
            findNavController().navigate(R.id.action_transferFragment_to_accountFragment)
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