package net.idey.tradernettest

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_qoute.view.*
import java.util.*
import kotlin.math.absoluteValue

@SuppressLint("SetTextI18n")
class QuoteHolder(view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var quote: Quote

    fun bind(q: Quote) {
        quote = q

        itemView.ticker.text = quote.c

        quote.pcp?.let {
            itemView.changePercent.setTextColor(itemView.context.getColor(provideChangeColor(it)))
            itemView.changePercent.text = provideChangeSymbol(it) + it.absoluteValue.getRounded() + "%"
        }

        setDisplayData(quote)

        //loadLogo(quote.c)
    }

    fun update(q: Quote) {
        q.pcp?.let { newPcp ->
            itemView.changePercent.text = provideChangeSymbol(newPcp) + newPcp.absoluteValue.getRounded() + "%"

            val indicatorBackground =
                if (newPcp > 0.0) R.drawable.indicator_green_bg else R.drawable.indicator_red_bg
            showIndicatorAnimation(indicatorBackground, provideChangeColor(newPcp))
        }
        quote = q
        setDisplayData(quote)
    }

    private fun loadLogo(ticker: String) {
        Glide
            .with(itemView.context)
            .load(IMAGE_BASE_URL + ticker.toLowerCase(Locale.getDefault()))
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.printStackTrace()
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource != null) {
                        itemView.ticker.setCompoundDrawables(resource, null, null, null)
                        //itemView.logo.setImageDrawable(resource)
                    }
                    return true
                }
            }).into(itemView.logo)
    }

    private fun setDisplayData(quote: Quote) {
        quote.ltr?.let { itemView.stock.text = it }
        quote.name?.let { itemView.name.text = it }
        quote.chg?.let { itemView.change.text = "(${it.getRounded()})" }
        quote.ltp?.let { itemView.price.text = it.getRounded() }
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

    private fun provideChangeColor(it: Double): Int {
        return when {
            it > 0 -> R.color.green
            it < 0 -> R.color.red
            else -> R.color.black
        }
    }

    private fun provideChangeSymbol(it: Double): String {
        return when {
            it > 0 -> "+"
            it < 0 -> "-"
            else -> ""
        }
    }

    companion object {
        private const val IMAGE_BASE_URL = "https://tradernet.ru/logos/get-logo-by-ticker?ticker="
    }

}

fun Double.getRounded(): String {
    return String.format("%.2f", this);
}