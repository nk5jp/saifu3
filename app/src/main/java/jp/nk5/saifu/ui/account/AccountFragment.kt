package jp.nk5.saifu.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import jp.nk5.saifu.Common
import jp.nk5.saifu.databinding.FragmentAccountBinding
import jp.nk5.saifu.service.AccountService
import jp.nk5.saifu.ui.util.AccountListAdapter
import jp.nk5.saifu.viewmodel.Observer
import jp.nk5.saifu.viewmodel.UpdateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountFragment : Fragment(), View.OnClickListener, Observer, AccountListAdapter.OnItemClickListener {

    private var _binding: FragmentAccountBinding? = null //レイアウト情報
    private val binding get() = _binding!!
    private val common by lazy {context?.applicationContext as Common}
    private val viewModel by lazy { common.accountViewModel } //ビューモデル
    private val service by lazy { AccountService(common.accountRepository, viewModel) } //サービス
    private val editText by lazy { binding.editText1 } //名称入力用のeditText
    private val recyclerView by lazy { binding.recyclerView1 } //口座リストのrecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addObserver(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.button1.setOnClickListener(this)

        recyclerView.adapter = AccountListAdapter(viewModel.accounts, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun updateView(updateTypes: List<UpdateType>) {
        for(updateType in updateTypes) {
            print(updateType)
        }
    }

    override fun onClick(view: View) {
        try {
            val name = editText.text.toString()
            //エラーチェック：空白は受け付けない
            if (name == "") { alert("口座名が未入力です"); return }
            CoroutineScope(Dispatchers.Main).launch {
                service.createAccount(name)
            }
        } catch (e: Exception) {
            alert(e.toString())
        }
    }

    override fun onItemLongClick(view: View): Boolean {
        TODO("Not yet implemented")
    }

    private fun alert(string: String)
    {
        Toast.makeText(activity, string, Toast.LENGTH_SHORT).show()
    }

}