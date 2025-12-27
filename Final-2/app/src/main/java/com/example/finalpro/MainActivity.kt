package com.example.finalpro

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // ... (變數宣告保持原樣) ...
    private lateinit var tvWelcome: TextView
    private lateinit var tvScore: TextView
    private lateinit var spinnerTheme: Spinner
    private lateinit var btnStart: Button
    private lateinit var btnRecords: Button
    private lateinit var btnFeedback: Button
    private lateinit var btnLogout: Button

    // ▼▼▼ 這裡就是 onCreate，我加了一行 createNotificationChannel() ▼▼▼
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ★ 1. 加入這行：一啟動就建立通知頻道
        createNotificationChannel()

        initViews()
        setupSpinner()
        setupListeners()
    }

    private fun initViews() {
        // ... (保持原樣) ...
        tvWelcome = findViewById(R.id.tv_welcome)
        tvScore = findViewById(R.id.tv_score)
        spinnerTheme = findViewById(R.id.spinner_theme)
        btnStart = findViewById(R.id.btn_start)
        btnRecords = findViewById(R.id.btn_records)
        btnFeedback = findViewById(R.id.btn_feedback)
        btnLogout = findViewById(R.id.btn_logout)
    }

    private fun setupSpinner() {
        val items = arrayOf("關卡1")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinnerTheme.adapter = adapter
    }

    private fun setupListeners() {
        // ... (其他按鈕監聽器保持原樣) ...
        btnStart.setOnClickListener {
            val selectedTheme = spinnerTheme.selectedItem.toString()
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("THEME", selectedTheme)
            startActivity(intent)
        }

        btnRecords.setOnClickListener {
            val intent = Intent(this, ScoreActivity::class.java)
            startActivity(intent)
        }

        btnFeedback.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        // ★ 2. 修改登出按鈕：加入 showNotification()
        btnLogout.setOnClickListener {
            showNotification() // 發送通知
            Toast.makeText(this, "已登出", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // ★ 3. 在最下方加入這兩個負責通知的新函式

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "登出通知"
            val descriptionText = "當使用者登出時發送通知"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_LOGOUT", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        // 檢查權限 (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
                return
            }
        }

        val builder = NotificationCompat.Builder(this, "CHANNEL_LOGOUT")
            .setSmallIcon(R.mipmap.ic_launcher_round) // 確保這裡有對應的小圖示
            .setContentTitle("再見！")
            .setContentText("您已成功登出，期待下次再來挑戰！")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
}