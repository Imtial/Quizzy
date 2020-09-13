package com.example.quizzy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Quiz::class, Question::class, Response::class], version = 3, exportSchema = false)
@TypeConverters(QuestionsConverter::class, ResponsesConverter::class, ListConverter::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract val quizDao : QuizDao
    abstract val questionDao : QuestionDao
    abstract val responseDao : ResponseDao

    companion object {
        @Volatile
        private lateinit var INSTANCE : QuizDatabase

        fun getDatabase(context: Context) : QuizDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            QuizDatabase::class.java,
                            "quiz_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(object : RoomDatabase.Callback(){
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)

                                }

                                override fun onOpen(db: SupportSQLiteDatabase) {
                                    super.onOpen(db)
//                                    val question1 = Question(2, "Hello", MULTIPLE, listOf("a", "b", "c"), 2F, listOf("b"))
//                                    val question2 = Question(3, "Bye", MULTIPLE, listOf("d", "e", "f"), 3F, listOf("d", "f"))
                                    CoroutineScope(Dispatchers.IO).launch {
                                        INSTANCE.questionDao.clearTable()
                                        INSTANCE.responseDao.clearTable()
                                    }
                                }
                            })
                            .build()
                }
            }
            return INSTANCE
        }
    }
}