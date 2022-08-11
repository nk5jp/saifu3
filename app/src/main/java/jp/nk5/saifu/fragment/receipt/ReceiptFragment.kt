package jp.nk5.saifu.fragment.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.nk5.saifu.MyFragment
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentReceiptBinding


class ReceiptFragment : MyFragment() {

    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }
}