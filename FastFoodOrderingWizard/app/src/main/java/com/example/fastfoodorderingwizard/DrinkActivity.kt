package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fastfoodorderingwizard.databinding.ActivityDrinkBinding

class DrinkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelect.setOnClickListener {
            val drink = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rbCola -> "可樂"
                R.id.rbTea -> "紅茶"
                R.id.rbJuice -> "果汁"
                else -> null
            }

            val resultIntent = Intent()
            resultIntent.putExtra("drink", drink)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
