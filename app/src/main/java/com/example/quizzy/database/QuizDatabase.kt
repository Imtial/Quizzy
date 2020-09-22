package com.example.quizzy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quizzy.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CachedQuiz::class, Question::class, Response::class, QuizItem::class, CachedUser::class],
        version = 15, exportSchema = false)
@TypeConverters(QuestionResponsesConverter::class, ResponsesConverter::class, ListConverter::class, DateConverter::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract val quizDao : QuizDao
    abstract val questionDao : QuestionDao
    abstract val responseDao : ResponseDao
    abstract val quizItemDao : QuizItemDao
    abstract val userDao: UserDao

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
                                    CoroutineScope(Dispatchers.IO).launch {
//                                        INSTANCE.questionDao.clearTable()
//                                        INSTANCE.responseDao.clearTable()
//                                        INSTANCE.quizItemDao.clearTable()
//                                        INSTANCE.quizDao.clearTable()
//                                        INSTANCE.userDao.clearTable()
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