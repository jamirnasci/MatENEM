package com.japps.matenem.questions

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.japps.matenem.entities.Question
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class LoadQuestions {
    @SuppressLint("CheckResult")
    fun load(count: Int): MutableList<Question>{
        val url: URL = URL("https://raw.githubusercontent.com/jamirnasci/mat_questions/refs/heads/main/questions.json")
        val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.connect()

        val response: StringBuilder = StringBuilder()
        if(httpURLConnection.responseCode == HttpURLConnection.HTTP_OK){
            val reader = InputStreamReader(httpURLConnection.inputStream)
            reader.forEachLine { line ->
                response.append(line)
            }
        }
        val gson: Gson = Gson()
        val type = object : TypeToken<MutableList<Question>>() {}.type
        val questionsList: MutableList<Question> = gson.fromJson(response.toString().trimIndent(), type)
        return randomQuestions(questionsList, count)
    }
    private fun randomQuestions(list: MutableList<Question>, count: Int): MutableList<Question>{

        val finalList: MutableList<Question> = ArrayList<Question>()
        for (i in 1 .. count){
            val index: Int = Random.nextInt(0, list.size - 1)
            finalList.add(list[index])
            list.removeAt(index)
        }
        return finalList
    }
}