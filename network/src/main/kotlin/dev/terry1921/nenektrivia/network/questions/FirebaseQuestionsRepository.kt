package dev.terry1921.nenektrivia.network.questions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.terry1921.nenektrivia.model.question.RemoteQuestion
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class FirebaseQuestionsRepository(
    private val database: FirebaseDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuestionsRepository {

    override suspend fun fetchQuestions(): List<RemoteQuestion> = withContext(dispatcher) {
        suspendCancellableCoroutine { continuation ->
            val query = database.reference.child("questions1")
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val questions = snapshot.children.mapNotNull { child ->
                        val id = child.child("id").getValue(String::class.java)
                            ?: child.key
                            ?: return@mapNotNull null
                        val question = child.child("question").getValue(String::class.java)
                            ?: return@mapNotNull null
                        val category = child.child("category").getValue(String::class.java)
                            ?: return@mapNotNull null
                        val answerGood = child.child("answergood").getValue(String::class.java)
                            ?: return@mapNotNull null
                        val answerBad01 = child.child("answerbad01").getValue(String::class.java)
                            ?: return@mapNotNull null
                        val answerBad02 = child.child("answerbad02").getValue(String::class.java)
                            ?: return@mapNotNull null
                        val answerBad03 = child.child("answerbad03").getValue(String::class.java)
                            ?: return@mapNotNull null
                        val tip = child.child("tip").getValue(String::class.java)

                        RemoteQuestion(
                            id = id,
                            question = question,
                            category = category,
                            answerGood = answerGood,
                            answerBad01 = answerBad01,
                            answerBad02 = answerBad02,
                            answerBad03 = answerBad03,
                            tip = tip
                        )
                    }

                    if (continuation.isActive) {
                        continuation.resume(questions)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(error.toException())
                    }
                }
            }

            query.addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation { query.removeEventListener(listener) }
        }
    }
}
