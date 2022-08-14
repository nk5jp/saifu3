package jp.nk5.saifu.ui.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.nk5.saifu.R
import jp.nk5.saifu.domain.Title

class TitleListAdapter(private val titles: List<TitleRow>)
    : RecyclerView.Adapter<TitleListAdapter.ViewHolder>()
{
    /**
     * 各行のview情報を保持するためのクラス
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView = view.findViewById(R.id.textView1)
    }

    /**
     * 各行のデータセット用に作成したデータクラス
     */
    data class TitleRow(val title: Title, val amount: Int)

    /**
     * レイアウト情報を設定する
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
            : ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_title, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * 各行のデータをバインドする
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView1.text = "%s: %,d円".format(
            titles[position].title.text,
            titles[position].amount
        )
    }

    /**
     * アイテム数を返却する
     */
    override fun getItemCount() = titles.size
}