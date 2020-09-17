package com.example.quizzy

import com.example.quizzy.domain.*

val QUIZITEMS = listOf(
        QuizItem("aXdEf9", "Fluid Mechanics: Chapter 4", 15, 15F, 1600662600000L, 25, 32,
                listOf("fluid mechanics", "physics", "mechanical"), 6F, 3.5F, PRIVATE,
                "Mir Mahathir",
                "https://media-exp1.licdn.com/dms/image/C5103AQGSOeQyFQr69g/profile-displayphoto-shrink_200_200/0?e=1605744000&v=beta&t=k-bZ0OM2oOwLBqR6dii6A-8EuhAsGS-RqiH3RIooF3U"),
        QuizItem("bMnefTyg", "Survey: Popular Dark Fantasies", 10, 0F, 0L, 0, 51,
                listOf("survey", "psychology"), 1F, 4F, PUBLIC,
                "Shahrar Swapnil",
                "https://scontent.fdac11-1.fna.fbcdn.net/v/t31.0-8/23120131_1674257369261923_4423364783504841056_o.jpg?_nc_cat=111&_nc_sid=730e14&_nc_ohc=XCpN0BmvYlgAX_PyMAw&_nc_ht=scontent.fdac11-1.fna&oh=c07bcf504d90472e614d018adbf6cb97&oe=5F833427"),

        QuizItem("45fjhkjeYB", "What Kind Of Spirit Are You?", 5, 10F, 0L, 10, 62,
                listOf("fun", "psychology"), 1F, 4.5F, PUBLIC,
                "Obaidul Kader",
                "https://d30fl32nd2baj9.cloudfront.net/media/2019/03/03/obaidul-quader-3.jpg/BINARY/Obaidul+Quader+3.jpg"),
        QuizItem("kkjHJK6g3", "Marriage Counselling Questionnaire", 12, 50F,
                1601128800000L, 0, 103, listOf("marriage", "counselling"),
                8F, 4.2F, PRIVATE, "Hasibul Hisham",
                "https://scontent.fdac11-1.fna.fbcdn.net/v/t1.0-1/c48.0.320.320a/p320x320/88241758_1407765116082453_8609546447760654336_o.jpg?_nc_cat=100&_nc_sid=7206a8&_nc_ohc=2B2bffN4HTUAX--sdub&_nc_ht=scontent.fdac11-1.fna&oh=c0a8513a21c2817c264bb2a6d9186816&oe=5F86AD53")
)

val QUIZZES = listOf(
        Quiz("aXdEf9", "Sample Quiz", listOf(Question(1, "True or False 3+3=6?", SINGLE, listOf("true", "false"), 1F, listOf("true")),
                Question(2, "What sound does a dog make?", MCQ, listOf("Meow", "Hoo", "Oink", "Woof"), 2F, listOf("Hoo", "Woof")),
                Question(3, "What is the full form of WHO?", TEXT, listOf(), 2F, listOf("World Health Organization"))),
                listOf(Response(0F, 3F, "OK"), Response(3F, 5F, "Good")), listOf(), NOPASSWORD, 1600662600000L, 1)
)

//val USERINFO = UserInfo("Asif Imtial", "aimtial@gmail.com", "jdkhfkJHFnb65156SAKjd", "123456")

fun keyGen(): String = System.currentTimeMillis().toString()
