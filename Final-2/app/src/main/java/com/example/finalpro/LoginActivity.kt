package com.example.finalpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    // 定義變數 (對應 XML 中的 ID)
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. 初始化元件
        initViews()

        // 2. 設定按鈕監聽器
        setupListeners()
    }

    private fun initViews() {
        // 綁定 XML 中的 ID
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
    }

    private fun setupListeners() {
        // --- 登入按鈕邏輯 ---
        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                // 如果欄位是空的，顯示警告
                Toast.makeText(this, "請輸入電子郵件和密碼", Toast.LENGTH_SHORT).show()
            } else {
                // 這裡可以加入檢查帳號密碼的邏輯
                Toast.makeText(this, "登入成功！", Toast.LENGTH_SHORT).show()

                // 跳轉到主畫面 (MainActivity)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // 關閉 LoginActivity，這樣按返回鍵才不會又回到登入頁
            }
        }

        // --- 註冊按鈕邏輯 ---
        btnRegister.setOnClickListener {
            Toast.makeText(this, "註冊成功", Toast.LENGTH_SHORT).show()
            // 如果有註冊頁面，可以在這裡寫跳轉程式碼
        }
    }
}