package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fastfoodorderingwizard.databinding.ActivityMainMealBinding

class MainMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelect.setOnClickListener {
            val meal = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rbBurger -> "漢堡"
                R.id.rbChicken -> "炸雞"
                R.id.rbPizza -> "披薩"
                else -> null
            }

            val resultIntent = Intent()
            resultIntent.putExtra("mainMeal", meal)
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // 回傳結果後關閉這個 Activity
        }
    }
}