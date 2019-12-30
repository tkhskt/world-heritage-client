package com.github.gericass.world_heritage_client.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

class PagingManager<T : Any>(
    private val responseType: KClass<T>
) {
    private val db = FirebaseFirestore.getInstance()

    private val limit = 50L

    var collectionPath: String = ""

    private val first by lazy {
        db.collection(collectionPath)
            .orderBy("created_at", Query.Direction.DESCENDING)
            .limit(limit)
    }
    private var next: Query? = null

    suspend fun getRecords(onSuccess: (List<T>) -> Unit, onFailure: () -> Unit) {
        val nextQuery = next
        if (nextQuery != null) {
            nextQuery.getWithPaging(onSuccess, onFailure)
        } else {
            first.getWithPaging(onSuccess, onFailure)
        }
    }

    private suspend fun Query.getWithPaging(onSuccess: (List<T>) -> Unit, onFailure: () -> Unit) {
        get()
            .addOnSuccessListener { recordSnapshots ->
                if (recordSnapshots.size() <= 0) return@addOnSuccessListener
                val lastVisible = recordSnapshots.documents[recordSnapshots.size() - 1]

                next = db.collection(collectionPath)
                    .orderBy("created_at", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(limit)

                onSuccess(recordSnapshots.map {
                    val record = it.toObject(responseType.java)
                    record
                })
            }.addOnFailureListener {
                onFailure()
            }
            .await()
    }
}