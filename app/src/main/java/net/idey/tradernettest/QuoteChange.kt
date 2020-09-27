package net.idey.tradernettest
import com.google.gson.annotations.SerializedName

data class QuoteItem(
    @SerializedName("q")
    val q: List<Quote>
)

//c, pcp, ltr, name, ltp, (chg)
data class Quote(
    @SerializedName("c")
    val c: String,
    @SerializedName("pcp")
    val pcp: Double?,
    @SerializedName("ltr")
    val ltr: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("ltp")
    val ltp: Double?,
    @SerializedName("chg")
    val chg: Double?
)