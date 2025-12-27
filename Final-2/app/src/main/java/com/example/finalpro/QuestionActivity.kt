package com.example.finalpro

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// --- 1. 定義 API 資料結構 ---
data class TriviaResponse(val results: List<TriviaQuestion>)
data class TriviaQuestion(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)

// --- 2. 定義 API 介面 ---
interface TriviaApiService {
    // 這裡設為 10 題，類別 9 (一般知識)
    @GET("api.php?amount=10&category=9&difficulty=medium&type=multiple")
    fun getQuestions(): Call<TriviaResponse>
}

class QuizActivity : AppCompatActivity() {

    // UI 元件
    private lateinit var tvQuestion: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnSubmit: Button
    private lateinit var radioButtons: List<RadioButton>

    // 遊戲數據
    private var questionList: List<TriviaQuestion> = ArrayList()
    private var currentQuestionIndex = 0
    private var correctAnsString: String = ""

    // ★★★ 新增：分數變數 ★★★
    private var currentScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        // 1. 初始化元件
        tvQuestion = findViewById(R.id.questionText)
        radioGroup = findViewById(R.id.optionsGroup)
        btnSubmit = findViewById(R.id.submitBtn)

        radioButtons = listOf(
            findViewById(R.id.opt1),
            findViewById(R.id.opt2),
            findViewById(R.id.opt3),
            findViewById(R.id.opt4)
        )

        setAllViewsEnabled(false)
        tvQuestion.text = "正在連線載入題目..."

        // 重置分數
        currentScore = 0

        // 2. 呼叫 API
        fetchQuestionsFromApi()

        // 3. 設定按鈕監聽
        btnSubmit.setOnClickListener {
            checkAnswer()
        }
    }

    private fun fetchQuestionsFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(TriviaApiService::class.java)

        api.getQuestions().enqueue(object : Callback<TriviaResponse> {
            override fun onResponse(call: Call<TriviaResponse>, response: Response<TriviaResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResult = response.body()!!
                    if (apiResult.results.isNotEmpty()) {
                        questionList = apiResult.results
                        currentQuestionIndex = 0
                        showQuestion()
                    } else {
                        tvQuestion.text = "題目載入失敗 (無資料)"
                    }
                } else {
                    tvQuestion.text = "伺服器錯誤: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<TriviaResponse>, t: Throwable) {
                tvQuestion.text = "連線失敗，請檢查網路。\n${t.message}"
                Log.e("QuizActivity", "API Error", t)
            }
        })
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questionList.size) {
            // 遊戲結束，不需要再顯示題目
            return
        }

        setAllViewsEnabled(true)
        radioGroup.clearCheck()

        val currentQ = questionList[currentQuestionIndex]

        // HTML 解碼並顯示題目
        val decodedQuestion = Html.fromHtml(currentQ.question, Html.FROM_HTML_MODE_LEGACY).toString()
        // 顯示目前題數與題目
        tvQuestion.text = "第 ${currentQuestionIndex + 1} 題 / 共 10 題\n\n$decodedQuestion"

        // 處理選項
        val allOptions = currentQ.incorrect_answers.toMutableList()
        allOptions.add(currentQ.correct_answer)
        allOptions.shuffle()

        for (i in radioButtons.indices) {
            if (i < allOptions.size) {
                val decodedOption = Html.fromHtml(allOptions[i], Html.FROM_HTML_MODE_LEGACY).toString()
                radioButtons[i].text = decodedOption
                radioButtons[i].visibility = View.VISIBLE
            } else {
                radioButtons[i].visibility = View.GONE
            }
        }

        // 儲存正確答案
        correctAnsString = currentQ.correct_answer
    }

    private fun checkAnswer() {
        val checkedId = radioGroup.checkedRadioButtonId
        if (checkedId == -1) {
            Toast.makeText(this, "請先選擇一個答案！", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedRb = findViewById<RadioButton>(checkedId)
        val selectedText = selectedRb.text.toString()
        val decodedCorrectAns = Html.fromHtml(correctAnsString, Html.FROM_HTML_MODE_LEGACY).toString()

        // --- 比對答案與計分 ---
        if (selectedText == decodedCorrectAns) {
            currentScore += 100 // 答對一題加 100 分
            Toast.makeText(this, "答對了！ 目前分數: $currentScore", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "答錯了！正確答案是: $decodedCorrectAns", Toast.LENGTH_SHORT).show()
        }

        // --- 進入下一題或結束 ---
        currentQuestionIndex++

        if (currentQuestionIndex < questionList.size) {
            // 還有題目，載入下一題
            if (currentQuestionIndex == questionList.size - 1) {
                btnSubmit.text = "完成並查看成績" // 最後一題改變按鈕文字
            }
            showQuestion()
        } else {
            // ★★★ 全部答完，儲存分數並離開 ★★★
            saveScoreToHistory(currentScore)
            Toast.makeText(this, "挑戰結束！最終分數: $currentScore", Toast.LENGTH_LONG).show()
            finish() // 回到主選單
        }
    }

    // ★★★ 儲存分數到 SharedPreferences 的函式 ★★★
    private fun saveScoreToHistory(score: Int) {
        val prefs = getSharedPreferences("GameData", MODE_PRIVATE)
        val oldHistory = prefs.getString("history", "")

        // 組合新字串 (例如 "100,200,300")
        val newHistory = if (oldHistory.isNullOrEmpty()) {
            "$score"
        } else {
            "$oldHistory,$score"
        }

        // 寫入手機儲存空間
        prefs.edit().putString("history", newHistory).apply()
        Log.d("QuizActivity", "Score saved: $score")
    }

    private fun setAllViewsEnabled(enabled: Boolean) {
        btnSubmit.isEnabled = enabled
        for (rb in radioButtons) {
            rb.isEnabled = enabled
        }
    }
}