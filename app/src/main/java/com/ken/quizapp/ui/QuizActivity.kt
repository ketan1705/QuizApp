package com.ken.quizapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ken.quizapp.R
import com.ken.quizapp.databinding.ActivityQuizBinding
import com.ken.quizapp.models.QuestionsModel

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var list: ArrayList<QuestionsModel>
    private var count: Int = 0
    private var score: Int = 0
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        addDataInList()

        binding.option1.setOnClickListener {
            nextQuestion(binding.option1.text.toString())
        }
        binding.option2.setOnClickListener {
            nextQuestion(binding.option2.text.toString())
        }
        binding.option3.setOnClickListener {
            nextQuestion(binding.option3.text.toString())

        }
        binding.option4.setOnClickListener {
            nextQuestion(binding.option4.text.toString())
        }
    }

    private fun nextQuestion(option: String) {
        timer?.cancel() // Cancel the current timer
        binding.progressBar.progress = 0 // Reset the progress bar

        if (count < list.size && list[count].answer.equals(option))
            score++
        count++
        if (count < list.size) {
            binding.quizData = list[count]
            startTimer()
        } else {
            val intent = Intent(this, ScoreActivity::class.java)
            intent.putExtra("SCORE", score)
            startActivity(intent)
            finish()
        }
    }

    private fun addDataInList() {
        list = ArrayList<QuestionsModel>()

        Firebase.firestore.collection("quiz").get().addOnSuccessListener { doct ->
            list.clear()
            for (i in doct.documents) {
                var questions = i.toObject(QuestionsModel::class.java)
                list.add(questions!!)
            }

            binding.quizData = list[0]
            startTimer()
        }

        /*    list.add(
                QuestionsModel(
                    "What is the primary programming language used for Android app development?",
                    "Python",
                    "Java",
                    "Kotlin",
                    "C++",
                    "Java" // Correct answer
                )
            )

            list.add(
                QuestionsModel(
                    "Which component is used to create a user interface in Android?",
                    "Activity",
                    "Service",
                    "Broadcast Receiver",
                    "Content Provider",
                    "Activity" // Correct answer
                )
            )

            list.add(
                QuestionsModel(
                    "What is the purpose of the AndroidManifest.xml file?",
                    "To define the layout of the app",
                    "To declare app components and permissions",
                    "To manage app resources",
                    "To handle user input",
                    "To declare app components and permissions" // Correct answer
                )
            )

            list.add(
                QuestionsModel(
                    "Which of the following is a lifecycle method of an Android Activity?",
                    "onCreate()",
                    "onStartActivity()",
                    "onResumeApp()",
                    "onStopActivity()",
                    "onCreate()" // Correct answer
                )
            )

            list.add(
                QuestionsModel(
                    "What is the use of Gradle in Android development?",
                    "To design the user interface",
                    "To manage app dependencies and build configurations",
                    "To handle network requests",
                    "To store app data",
                    "To manage app dependencies and build configurations" // Correct answer
                )
            )
            uploadQuestions()
            binding.quizData = list[0]
    */
    }

    fun uploadQuestions() {

        for (question in list) {
            Firebase.firestore.collection("quiz").add(question)
                .addOnSuccessListener { documentReference ->
                    val id = documentReference.id
                    Log.i("Firebase Questions Id: ", id)
                }
                .addOnFailureListener { e ->
                    Log.w("Firebase Questions: ", "Error adding document", e)
                }
        }

    }

    private fun startTimer() {
        // 30 seconds countdown with 1 second interval
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Calculate the progress based on time remaining
                val progress = ((30000 - millisUntilFinished) / 1000).toInt()
                binding.progressBar.progress = progress
            }

            override fun onFinish() {
                // When the countdown finishes, set the progress to 30 (max)
                binding.progressBar.progress = 30
                nextQuestion("")
            }
        }.start()
    }

}