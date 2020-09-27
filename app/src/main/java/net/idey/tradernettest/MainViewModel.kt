package net.idey.tradernettest

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel(), LifecycleObserver {

    private lateinit var socket: Socket
    private val gson = Gson()

    val uiData: LiveData<List<Quote>>
        get() = _uiData

    private val _uiData = MutableLiveData<List<Quote>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        socket = IO.socket(url)
        socket.on(Socket.EVENT_CONNECT, ::onConnect)
        socket.on("q", ::onQuoteReceived)
        socket.connect()
    }

    private fun onConnect(array: Array<Any>) {
        socket.emit("sup_updateSecurities2", JSONArray(tickers))
    }

    private fun onQuoteReceived(array: Array<Any>) {
        val json = (array.first() as JSONObject).toString()
        Log.d("MyLogTag", "Json $json")
        val response = gson.fromJson(json, QuoteItem::class.java)

        if (_uiData.value != null) {
            val list = ArrayList(_uiData.value!!)

            for (quote in response.q) {
                val index = list.indexOfFirst { it.c == quote.c }

                if (index == -1) {
                    list.add(quote)
                } else {
                    list[index] = quote
                }
            }

            _uiData.postValue(list)
        } else {
            _uiData.postValue(response.q)
        }
    }

    override fun onCleared() {
        socket.disconnect()
        super.onCleared()
    }

    companion object {
        private const val url = "https://ws3.tradernet.ru/"
        private val tickers =
            "RSTI,GAZP,MRKZ,RUAL,HYDR,MRKS,SBER,FEES,TGKA,VTBR,ANH.US,VICL.US,BURG. US,NBL.US,YETI.US,WSFS.US,NIO.US,DXC.US,MIC.US,HSBC.US,EXPN.EU,GSK.EU,SH P.EU,MAN.EU,DB1.EU,MUV2.EU,TATE.EU,KGF.EU,MGGT.EU,SGGD.EU".split(
                ","
            )
    }

}