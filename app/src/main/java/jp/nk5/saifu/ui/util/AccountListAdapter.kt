package jp.nk5.saifu.ui.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.nk5.saifu.R
import jp.nk5.saifu.domain.Account

class AccountListAdapter(private val dataSet: List<Account>, private val listener: OnItemClickListener)
    : RecyclerView.Adapter<AccountListAdapter.ViewHolder>(), View.OnLongClickListener  {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView = view.findViewById(R.id.textView1)
        val textView2: TextView = view.findViewById(R.id.textView2)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_account, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView1.text = dataSet[position].name
        viewHolder.textView2.text = dataSet[position].amount.toString()
        viewHolder.textView1.setOnLongClickListener(this)
        viewHolder.textView2.setOnLongClickListener(this)
    }

    override fun getItemCount() = dataSet.size

    override fun onLongClick(view: View): Boolean
    {
        return listener.onItemLongClick(view.parent as View)
    }

    interface OnItemClickListener {
        fun onItemLongClick(view: View): Boolean
    }

}