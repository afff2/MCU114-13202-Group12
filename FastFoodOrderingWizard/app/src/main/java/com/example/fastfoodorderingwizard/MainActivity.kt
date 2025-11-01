package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fastfoodorderingwizard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mainMeal: String? = null
    private var sideDish: String? = null
    private var drink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainMeal.setOnClickListener {
            val intent = Intent(this, MainMealActivity::class.java)
            startActivityForResult(intent, 100) // 主餐請求碼 100
        }

        binding.btnSideDish.setOnClickListener {
            val intent = Intent(this, SideDishActivity::class.java)
            startActivityForResult(intent, 101) // 副餐請求碼 101
        }

        binding.btnDrink.setOnClickListener {
            val intent = Intent(this, DrinkActivity::class.java)
            startActivityForResult(intent, 102) // 飲料請求碼 102
        }

        binding.btnConfirm.setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java)
            intent.putExtra("mainMeal", mainMeal)
            intent.putExtra("sideDish", sideDish)
            intent.putExtra("drink", drink)
            startActivity(intent)
        }

        updateOrderText()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                100 -> mainMeal = data.getStringExtra("mainMeal")
                101 -> sideDish = data.getStringExtra("sideDish")
                102 -> drink = data.getStringExtra("drink")
            }
            updateOrderText()
        }
    }

    private fun updateOrderText() {
        binding.tvOrder.text = "主餐: ${mainMeal ?: "未選"}\n副餐: ${sideDish ?: "未選"}\n飲料: ${drink ?: "未選"}"
    }
}
