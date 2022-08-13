package jp.nk5.saifu.ui.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.nk5.saifu.R
import jp.nk5.saifu.domain.ReceiptDetail
import jp.nk5.saifu.domain.TaxType

class DetailListAdapter(
    private val details: List<ReceiptDetail>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<DetailListAdapter.ViewHolder>(),
    View.OnClickListener,
    View.OnLongClickListener
{
    /**
     * 各行のview情報を保持するためのクラス
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView = view.findViewById(R.id.textView1)
        val textView2: TextView = view.findViewById(R.id.textView2)
        val textView3: TextView = view.findViewById(R.id.textView3)
    }

    /**
     * レイアウト情報を設定する
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_detail, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * 各行のデータをバインドする
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView1.text = details[position].title.text
        viewHolder.textView2.text = "%,d円".format(details[position].amount)
        when(details[position].taxType) {
            TaxType.INCLUDE -> {
                changeRowColor(viewHolder, Color.WHITE)
                viewHolder.textView3.text = ""
            }
            TaxType.EXCLUDE_EIGHT -> {
                changeRowColor(viewHolder, Color.YELLOW)
                viewHolder.textView3.text = "(+8%)"
            }
            TaxType.EXCLUDE_TEN -> {
                changeRowColor(viewHolder, Color.CYAN)
                viewHolder.textView3.text = "(+10%)"
            }
        }
        viewHolder.textView1.setOnLongClickListener(this)
        viewHolder.textView1.setOnClickListener(this)
        viewHolder.textView2.setOnLongClickListener(this)
        viewHolder.textView2.setOnClickListener(this)
        viewHolder.textView3.setOnLongClickListener(this)
        viewHolder.textView3.setOnClickListener(this)
    }

    private fun changeRowColor(viewHolder: ViewHolder, colorId: Int) {
        viewHolder.textView1.setBackgroundColor(colorId)
        viewHolder.textView2.setBackgroundColor(colorId)
        viewHolder.textView3.setBackgroundColor(colorId)
    }

    /**
     * アイテム数を返却する
     */
    override fun getItemCount() = details.size

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