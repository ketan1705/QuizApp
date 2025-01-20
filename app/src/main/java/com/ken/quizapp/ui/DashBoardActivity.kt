package com.ken.quizapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ken.quizapp.R
import com.ken.quizapp.adapter.QuizCategoryAdapter
import com.ken.quizapp.databinding.ActivityDashBoardBinding
import com.ken.quizapp.models.CategoryModel
import com.ken.quizapp.models.QuestionsModel
import java.io.InputStream

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)

        val list = ArrayList(
            listOf<CategoryModel>(
                CategoryModel("1", "java"),
                CategoryModel("2", "kotlin"),
                CategoryModel("3", "c++"),
                CategoryModel("4", "c")
            )
        )

        binding.adapter = QuizCategoryAdapter(
            list, this
        )
//        storeDataInFirestore(list, questions)
//        storeSingleDataInFirestore(list)
        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // this is for storing data in firestore
    fun storeSingleDataInFirestore(
        list: ArrayList<CategoryModel>,
    ) {
        list.forEach { category ->
            val collectionRef =
                Firebase.firestore.collection("quiz").document(category.title.toString())
                    .collection("questions")
            val questions = loadQuestionsFromJson(category.title.toString(), this)
            questions.forEach { questions ->
                collectionRef.add(
                    mapOf(
                        "question" to questions.question,
                        "option1" to questions.option1,
                        "option2" to questions.option2,
                        "option3" to questions.option3,
                        "option4" to questions.option4,
                        "correctOption" to questions.answer
                    )
                )
                    .addOnSuccessListener { documentReference ->
                        val data = documentReference.id
                        Log.d("DashboardActivity", "storeDataInFirestore: $data")
                    }
            }
        }
    }

    // this is for storing data in firestore
    /*  fun storeDataInFirestore(list: ArrayList<CategoryModel>, questions: List<QuestionsModel>) {

          list.forEach { category ->
              val collecionRef =
                  Firebase.firestore.collection("quiz").document(category.title.toString())
                      .collection("questions")
  //            collecionRef.add {
              questions.forEach { questions ->
                  collecionRef.add(
                      mapOf(
                          "question" to questions.question,
                          "option1" to questions.option1,
                          "option2" to questions.option2,
                          "option3" to questions.option3,
                          "option4" to questions.option4,
                          "correctOption" to questions.answer
                      )
                  )
                      .addOnSuccessListener { documentReference ->
                          val data = documentReference.id
                          Log.d("DashboardActivity", "storeDataInFirestore: $data")
                      }
              }
          }

      }
  */
    fun loadQuestionsFromJson(type: String, context: Context): List<QuestionsModel> {
        var inputStream: InputStream = context.resources.openRawResource(R.raw.java_questions)
        if (type == "java")
            inputStream = context.resources.openRawResource(R.raw.java_questions)
        else if (type == "c++")
            inputStream = context.resources.openRawResource(R.raw.cpp_questions)
        else if (type == "c")
            inputStream = context.resources.openRawResource(R.raw.c_questions)
        else if (type == "kotlin")
            inputStream = context.resources.openRawResource(R.raw.kotlin_questions)
        val json = inputStream.bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<QuestionsModel>>() {}.type
        return Gson().fromJson(json, listType)
    }
}