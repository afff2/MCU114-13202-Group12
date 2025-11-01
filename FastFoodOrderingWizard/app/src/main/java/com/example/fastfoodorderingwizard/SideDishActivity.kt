package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fastfoodorderingwizard.databinding.ActivitySideDishBinding

class SideDishActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySideDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySideDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelect.setOnClickListener {
            val side = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rbFries -> "薯條"
                R.id.rbSalad -> "沙拉"
                R.id.rbNuggets -> "雞塊"
                else -> null
            }

            val resultIntent = Intent()
            resultIntent.putExtra("sideDish", side)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}