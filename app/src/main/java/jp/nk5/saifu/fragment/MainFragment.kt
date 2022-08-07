package jp.nk5.saifu.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import jp.nk5.saifu.R
import jp.nk5.saifu.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}