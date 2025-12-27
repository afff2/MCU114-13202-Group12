package com.example.finalpro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val listView = findViewById<ListView>(R.id.scoreListView)
        val btnBack = findViewById<Button>(R.id.btn_back) // 綁定返回按鈕

        // --- 1. 實作返回按鈕功能 ---
        btnBack.setOnClickListener {
            finish() // 這是 Android 標準的「關閉目前頁面」，會自動回到上一頁 (MainActivity)
        }

        // --- 2. 從 SharedPreferences 讀取真實資料 ---
        val dataList = loadScoreHistory()

        // 3. 設定 Adapter
        val adapter = MyScoreAdapter(dataList)
        listView.adapter = adapter
    }

    // 讀取歷史紀錄的函式
    private fun loadScoreHistory(): List<ScoreRecord> {
        val list = ArrayList<ScoreRecord>()

        // 取得儲存空間 (名稱要跟 QuizActivity 存的一樣是 "GameData")
        val prefs = getSharedPreferences("GameData", MODE_PRIVATE)
        val historyString = prefs.getString("history", "")

        if (!historyString.isNullOrEmpty()) {
            // 把字串切開 (例如 "100,200" 變成 ["100", "200"])
            val scores = historyString.split(",")

            // 倒序迴圈 (讓最新的成績顯示在最上面)
            for (i in scores.indices.reversed()) {
                val score = scores[i]
                // 產生顯示的文字
                list.add(ScoreRecord("第 ${i + 1} 次挑戰", "獲得分數: $score 分"))
            }
        } else {
            // 如果完全沒有紀錄，顯示一行提示
            list.add(ScoreRecord("尚無紀錄", "快去挑戰第一次吧！"))
        }

        return list
    }

    // --- 資料結構 ---
    data class ScoreRecord(val title: String, val subtitle: String)

    // --- Adapter (跟之前一樣，不用動) ---
    inner class MyScoreAdapter(private val list: List<ScoreRecord>) : BaseAdapter() {
        override fun getCount(): Int = list.size
        override fun getItem(position: Int): Any = list[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_score, parent, false)
            val title = view.findViewById<TextView>(R.id.tv_title)
            val subtitle = view.findViewById<TextView>(R.id.tv_subtitle)

            val item = list[position]
            title.text = item.title
            subtitle.text = item.subtitle
            return view
        }
    }
}