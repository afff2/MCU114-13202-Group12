package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fastfoodorderingwizard.databinding.ActivityConfirmBinding

class ConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBinding

    private var mainMeal: String? = null
    private var sideDish: String? = null
    private var drink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 取得從 MainActivity 傳來的餐點
        mainMeal = intent.getStringExtra("mainMeal")
        sideDish = intent.getStringExtra("sideDish")
        drink = intent.getStringExtra("drink")

        // 顯示餐點
        binding.tvOrder.text = "主餐: ${mainMeal ?: "未選"}\n副餐: ${sideDish ?: "未選"}\n飲料: ${drink ?: "未選"}"

        binding.btnConfirm.setOnClickListener {
            if (mainMeal.isNullOrEmpty() || sideDish.isNullOrEmpty() || drink.isNullOrEmpty()) {
                Toast.makeText(this, "請選擇主餐、副餐與飲料！", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("確認訂單")
                .setMessage("主餐: $mainMeal\n副餐: $sideDish\n飲料: $drink\n\n是否提交？")
                .setPositiveButton("提交") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(this, "訂單已送出！", Toast.LENGTH_SHORT).show()

                    // 將餐點回傳給 MainActivity
                    val resultIntent = Intent()
                    resultIntent.putExtra("mainMeal", mainMeal)
                    resultIntent.putExtra("sideDish", sideDish)
                    resultIntent.putExtra("drink", drink)
                    setResult(Activity.RESULT_OK, resultIntent)

                    // 關閉 ConfirmActivity 回到 MainActivity
                    finish()
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }
}