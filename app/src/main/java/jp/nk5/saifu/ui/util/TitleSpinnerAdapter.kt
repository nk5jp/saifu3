package jp.nk5.saifu.ui.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import jp.nk5.saifu.domain.Title

class TitleSpinnerAdapter: BaseAdapter() {
    private val titles = Title.titles

    /**
     * データの要素数を返却する
     */
    override fun getCount() = titles.size

    /**
     * 指定した行の費用科目のインスタンスを返却する
     */
    override fun getItem(position: Int) = titles[position]

    /**
     * 指定した行の費用科目のidを返却する
     * 費用科目のidはIntなので、Longに変換する必要がある
     */
    override fun getItemId(position: Int) = titles[position].id.toLong()

    /**
     * ドロップダウンを開く前（もしくは選択後）のView情報を返却する
     * convertViewには再利用のためのviewが保持されているので、非nullかつコンバートできるなら活用する
     * さもなくばインフレータからviewを新規生成して返却する
     * ちなみにparentはAppCompatSpinner、rootはMaterialTextView
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null && parent != null) {
            //デフォルトで用意されているspinner用のレイアウトをinflateする
            val root = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_spinner_item, parent, false)
            //デフォルトのレイアウトに用意されているtextViewを見つけ出し、文字列に口座名を設定する
            val textView = root.findViewById(android.R.id.text1) as TextView
            textView.text = titles[position].text
            root
        } else {
            convertView!!
        }
    }

    /**
     * ドロップダウンを開いたときに表示されるリストの各行のView情報を返却する
     * convertViewには再利用のためのviewが保持されているので、非nullかつコンバートできるなら活用する
     * さもなくばインフレータからviewを新規生成して返却する
     * ちなみにparentはAppCompatSpinner、rootはAppCompatCheckedTextView
     * デバッグしたところなぜか同じポジションを引数として複数回呼び出されるが理由は不明
     */
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null && parent != null) {
            //デフォルトで用意されているspinner用のレイアウトをinflateする
            val root = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
            //デフォルトのレイアウトに用意されているtextViewを見つけ出し、文字列に口座名を設定する
            val textView = root.findViewById(android.R.id.text1) as TextView
            textView.text = titles[position].text
            root
        } else {
            convertView!!
        }
    }

}