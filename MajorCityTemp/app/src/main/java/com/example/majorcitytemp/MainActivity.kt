package com.example.majorcitytemp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : AppCompatActivity() {

    data class WeatherResponse(val main: MainData, val name: String)
    data class MainData(val temp: Double, val humidity: Int)

    // 定義城市資料： Pair(顯示名稱, API查詢名稱)
    private val cityList = listOf(
        "New Taipei City 新北市" to "New Taipei City,TW",
        "Taoyuan City 桃園市" to "Taoyuan City,TW",
        "Hsinchu City 新竹市" to "Hsinchu City,TW",
        "Miaoli County 苗栗縣" to "Miaoli,TW",
        "Taichung City 台中市" to "Taichung,TW",
        "Changhua County 彰化縣" to "Changhua,TW",
        "Nantou County 南投縣" to "Nantou,TW",
        "Yunlin County 雲林縣(斗六)" to "Douliu,TW",
        "Chiayi City 嘉義市" to "Chiayi City,TW",
        "Tainan City 台南市" to "Tainan,TW",
        "Kaohsiung City 高雄市" to "Kaohsiung,TW",
        "Pingtung County 屏東縣" to "Pingtung,TW",
        "Yilan County 宜蘭縣" to "Yilan,TW",
        "Hualien County 花蓮縣" to "Hualien,TW",
        "Taitung County 台東縣" to "Taitung,TW",
        "Keelung City 基隆市" to "Keelung,TW",
        "Penghu County 澎湖縣(馬公)" to "Magong,TW",
        "Kinmen County 金門縣(金城)" to "Jincheng,TW",
        "Lianjiang County 連江縣(南竿)" to "Nangan,TW"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spnCity = findViewById<Spinner>(R.id.spnCity)
        val btnGetTemp = findViewById<Button>(R.id.btnGetTemp)

        // 1. 設定 Spinner (選單) 的內容
        // 我們只需要顯示名稱 (cityList 的 key/first 部分)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            cityList.map { it.first } // 取出所有的 "New Taipei City 新北市" 等名稱
        )
        spnCity.adapter = adapter

        // 2. 按鈕點擊事件
        btnGetTemp.setOnClickListener {
            // 取得目前使用者選到了第幾個選項
            val selectedIndex = spnCity.selectedItemPosition

            // 根據索引，從 cityList 拿出對應的資料
            val selectedCityPair = cityList[selectedIndex]

            val displayName = selectedCityPair.first  // 顯示名稱 (例如: 桃園市)
            val queryName = selectedCityPair.second   // 查詢代碼 (例如: Taoyuan City,TW)

            // 呼叫抓取天氣的函式，只傳入選中的那個城市
            fetchSingleCityWeather(displayName, queryName)
        }
    }

    // 抓取「單一」城市天氣的函式
    private fun fetchSingleCityWeather(displayName: String, queryName: String) {
        val apiKey = BuildConfig.OPEN_WEATHER_API_KEY

        if (apiKey.isEmpty()) {
            showAlertDialog("Error", "API Key is missing.")
            return
        }

        val url = "https://api.openweathermap.org/data/2.5/weather?q=$queryName&units=metric&appid=$apiKey"
        val client = OkHttpClient()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        val gson = Gson()
                        val weatherData = gson.fromJson(responseData, WeatherResponse::class.java)

                        withContext(Dispatchers.Main) {
                            // 顯示單一城市的結果
                            showAlertDialog(
                                displayName, // 標題顯示我們選單上的中文名稱
                                "Location: ${weatherData.name}\n" +
                                        "Temperature: ${weatherData.main.temp}°C\n" +
                                        "Humidity: ${weatherData.main.humidity}%"
                            )
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showAlertDialog("Error", "Failed to get data for $displayName. Code: ${response.code}")
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    showAlertDialog("Error", "Network error: ${e.message}")
                }
            }
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}