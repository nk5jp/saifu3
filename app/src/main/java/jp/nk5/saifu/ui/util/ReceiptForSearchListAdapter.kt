package jp.nk5.saifu.ui.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.nk5.saifu.R
import jp.nk5.saifu.domain.Receipt

class ReceiptForSearchListAdapter(private val receipts: List<Receipt>)
    : RecyclerView.Adapter<ReceiptForSearchListAdapter.ViewHolder>()
{
    /**
     * 各行のview情報を保持するためのクラス
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView = view.findViewById(R.id.textView1)
    }

    /**
     * レイアウト情報を設定する
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
    : ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_receipt, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * 各行のデータをバインドする
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView1.text = "%02d/%02d: %,d円".format(
            receipts[position].date.month,
            receipts[position].date.day,
            receipts[position].sum()[0]
        )
    }

    /**
     * アイテム数を返却する
     */
    override fun getItemCount() = receipts.size
}