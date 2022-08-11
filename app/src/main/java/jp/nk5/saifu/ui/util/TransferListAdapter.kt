package jp.nk5.saifu.ui.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.nk5.saifu.R
import jp.nk5.saifu.domain.Transfer

class TransferListAdapter(
    private val dataSet: List<Transfer>
) : RecyclerView.Adapter<TransferListAdapter.ViewHolder>() {
    /**
     * 各行のview情報を保持するためのクラス
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView = view.findViewById(R.id.textView1)
    }

    /**
     * レイアウト情報を設定する
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_transfer, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * 各行のデータをバインドする
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (dataSet[position].credit == null) {
            //貸方がnullの場合：入金のフォーマットで記述
            viewHolder.textView1.text = "%d日：%,d円(%sに入金)".format(
                dataSet[position].date.day,
                dataSet[position].amount,
                dataSet[position].debit.name
            )
        } else {
            //貸方がnullではない場合：振替のフォーマットで記述
            viewHolder.textView1.text = "%d日：%,d円(%s ⇒ %s)".format(
                dataSet[position].date.day,
                dataSet[position].amount,
                dataSet[position].credit!!.name,
                dataSet[position].debit.name
            )
        }
    }

    /**
     * アイテム数を返却する
     */
    override fun getItemCount() = dataSet.size
}