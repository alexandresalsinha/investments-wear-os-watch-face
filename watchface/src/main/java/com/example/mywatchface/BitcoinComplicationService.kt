package com.example.mywatchface

import android.util.Log
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

class BitcoinComplicationService : SuspendingComplicationDataSourceService() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        Log.d(TAG, "onComplicationRequest called")
        return try {
            val priceStr = fetchBitcoinPrice()
            if (priceStr != null) {
                Log.d(TAG, "Successfully fetched price: $priceStr")
                ShortTextComplicationData.Builder(
                    text = PlainComplicationText.Builder(priceStr).build(),
                    contentDescription = PlainComplicationText.Builder("Bitcoin price").build()
                ).build()
            } else {
                Log.w(TAG, "Price fetched was null")
                NoDataComplicationData()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onComplicationRequest", e)
            NoDataComplicationData()
        }
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData {
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder("$50,000").build(),
            contentDescription = PlainComplicationText.Builder("Bitcoin price").build()
        ).build()
    }

    private suspend fun fetchBitcoinPrice(): String? = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val price = json.getString("price").toDouble()
                "$${price.roundToInt()}"
            } else {
                Log.e(TAG, "Binance API error: ${connection.responseCode}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch Bitcoin price", e)
            null
        }
    }

    companion object {
        private const val TAG = "BitcoinComplication"
    }
}
