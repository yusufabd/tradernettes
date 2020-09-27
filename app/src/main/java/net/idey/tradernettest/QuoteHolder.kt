package net.idey.tradernettest

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_qoute.view.*

@SuppressLint("SetTextI18n")
class QuoteHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(quote: Quote) {
        itemView.ticker.text = quote.c
        quote.pcp?.let { itemView.changePercent.text = "${it}%" }
        quote.ltr?.let { itemView.stock.text = it }
        quote.name?.let { itemView.name.text = it }
        quote.chg?.let { itemView.change.text = "($it)" }
        quote.ltp?.let { itemView.price.text = it.toString() }
    }

    fun update(quote: Quote) {
        //todo update ui
        quote.pcp?.let {
            itemView.changePercent.text = "${it}%"
            val indicatorBackground =
                if (it > 0) R.drawable.indicator_green_bg else R.drawable.indicator_red_bg
            val textColor = if (it >= 0) R.color.green else R.color.red
            showIndicatorAnimation(indicatorBackground, textColor)
        }
        quote.ltr?.let { itemView.stock.text = it }
        quote.name?.let { itemView.name.text = it }
        quote.chg?.let { itemView.change.text = "($it)" }
        quote.ltp?.let { itemView.price.text = it.toString() }
    }

    private fun showIndicatorAnimation(indicatorBackground: Int, textColor: Int) {
        itemView.changePercent.setTextColor(itemView.context.getColor(R.color.white))
        itemView.indicator.setBackgroundResource(indicatorBackground)
        itemView.indicator.animate().setDuration(200).alpha(1f)

        Handler(Looper.getMainLooper()).postDelayed({
            itemView.indicator.animate().setDuration(200).alpha(0f)
            itemView.changePercent.setTextColor(itemView.context.getColor(textColor))
        }, 2000)
    }

}