package jp.nk5.saifu.ui.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.nk5.saifu.R
import jp.nk5.saifu.domain.Receipt

class ReceiptListAdapter(
    private val receipts: List<Receipt>,
    private val listener: OnItemClickListener
):
    RecyclerView.Adapter<ReceiptListAdapter.ViewHolder>(),
    View.OnClickListener,
    View.OnLongClickListener
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
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView1.text = "%,d円(%s)".format(
            receipts[position].sum()[0],
            receipts[position].account.name
        )
        viewHolder.textView1.setOnLongClickListener(this)
        viewHolder.textView1.setOnClickListener(this)
    }

    /**
     * アイテム数を返却する
     */
    override fun getItemCount() = receipts.size

    /**
     * コンストラクタで指定したリスナーに処理を移譲
     * viewは各textViewだが各行を指定したいのでparentを渡している
     */
    override fun onLongClick(view: View): Boolean
    {
        return listener.onItemLongClick(view.parent as View)
    }

    /**
     * コンストラクタで指定したリスナーに処理を移譲
     * viewは各textViewだが各行を指定したいのでparentを渡している
     */
    override fun onClick(view: View)
    {
        return listener.onItemClick(view.parent as View)
    }

    /**
     *　選択および長押し処理を移譲するためのインタフェース
     */
    interface OnItemClickListener {
        fun onItemClick(view: View)
        fun onItemLongClick(view: View): Boolean
    }
}