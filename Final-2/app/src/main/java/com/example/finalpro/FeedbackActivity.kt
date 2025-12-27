package com.example.finalpro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback) // 連結剛剛做的 XML

        // 1. 綁定元件
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val etFeedback = findViewById<EditText>(R.id.et_feedback)
        val btnSubmit = findViewById<Button>(R.id.btn_submit_feedback)

        // 2. 設定按鈕點擊事件
        btnSubmit.setOnClickListener {
            // 取得使用者輸入的資料
            val stars = ratingBar.rating // 取得星星數 (Float)
            val feedbackText = etFeedback.text.toString() // 取得文字

            // 簡單的檢查：如果沒有輸入內容或沒給星
            if (stars == 0f) {
                Toast.makeText(this, "請給我們一點評分星星喔！", Toast.LENGTH_SHORT).show()
            } else {
                // 模擬送出成功
                Toast.makeText(this, "感謝您的回饋！(評分: ${stars.toInt()} 星)", Toast.LENGTH_LONG).show()

                // 清空輸入框 (選用)
                etFeedback.text.clear()

                // 關閉目前頁面，回到主選單
                finish()
            }
        }
    }
}