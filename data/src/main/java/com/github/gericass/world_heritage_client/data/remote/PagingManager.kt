package com.github.gericass.world_heritage_client.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

class PagingManager<T : Any>(
    private val responseType: KClass<T>
) {
    val db = FirebaseFirestore.getInstance()

    private val limit = 50L

    var collectionPath: String = ""

    val first by lazy {
        db.collection(collectionPath).limit(limit)
    }
    var next: Query? = null

    suspend fun getRecords(onSuccess: (List<T>) -> Unit, onFailure: () -> Unit) {
        val nextQuery = next
        if (nextQuery != null) {
            nextQuery.getWithPaging(onSuccess, onFailure)
        } else {
            first.getWithPaging(onSuccess, onFailure)
        }
    }

    fun refresh() {
        next = null
    }

    private suspend fun Query.getWithPaging(onSuccess: (List<T>) -> Unit, onFailure: () -> Unit) {
        get()
            .addOnSuccessListener { recordSnapshots ->
                val lastVisible = recordSnapshots.documents[recordSnapshots.size() - 1]

                next = db.collection(collectionPath)
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