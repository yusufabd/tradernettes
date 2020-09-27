package net.idey.tradernettest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class MainAdapter : RecyclerView.Adapter<QuoteHolder>(){

    private val quotes = ArrayList<Quote>()

    fun setData(list: List<Quote>) {
        when {
            quotes.isEmpty() -> {
                quotes.addAll(list)
                notifyDataSetChanged()
            }
            list.isEmpty() -> {
                quotes.clear()
                notifyDataSetChanged()
            }
            else -> {
                val diffCallback = DiffCallback(quotes, list)
                val result = DiffUtil.calculateDiff(diffCallback, false)
                result.dispatchUpdatesTo(this)
                this.quotes.clear()
                this.quotes.addAll(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_qoute, parent, false)
        return QuoteHolder((view))
    }

    override fun onBindViewHolder(holder: QuoteHolder, position: Int) {
        holder.bind(quotes[position])
    }

    override fun onBindViewHolder(holder: QuoteHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads.first() is Quote) {
            val quote = payloads.first() as Quote
            holder.update(quote)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    class DiffCallback(
        private val oldList: List<Quote>,
        private val newList: List<Quote>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.c == newItem.c
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            if (oldItem != newItem) {
                return newItem
            }

            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

}