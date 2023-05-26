package com.example.opencontrol.domain

import com.example.opencontrol.model.AnswerNetwork
import com.example.opencontrol.model.QuestionNetwork
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyApi {

    @POST("/question")
    suspend fun getAnswerFromBot(@Body question: QuestionNetwork): AnswerNetwork
}