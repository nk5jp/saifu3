package jp.nk5.saifu.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentSearchDetailBinding
import jp.nk5.saifu.service.ReceiptService
import jp.nk5.saifu.ui.MyFragment
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.UpdateType
import jp.nk5.saifu.viewmodel.receipt.ReceiptViewModel

class SearchDetailFragment : MyFragment(), Observer {

    /**
     * 処理で使用するパラメータ群
     */
    private var _binding: FragmentSearchDetailBinding? = null
    private val binding get() = _binding!! //レイアウト情報
    private val viewModel by lazy { ReceiptViewModel() } //本画面のviewModel
    private val service by lazy { //本画面のservice
        ReceiptService(
            common.accountRepository,
            common.receiptRepository,
            viewModel
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_detail, container, false)
    }

    override suspend fun updateView(updateTypes: List<UpdateType>) {
        TODO("Not yet implemented")
    }
}