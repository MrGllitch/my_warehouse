package eu.glitch.mywarehouse.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.tasks.await

import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth) : IRepository {
    override suspend fun insertUnit(unit: WarehouseUnit) {
        firestore.collection("users").document(auth.currentUser!!.uid).collection("units").add(unit)
    }

    override suspend fun getUnits(): List<WarehouseUnit> {
        val units : MutableList<WarehouseUnit> = mutableListOf()
        try {
            val result = firestore.collection("users")
                .document(auth.currentUser!!.uid)
                .collection("units")
                .get()
                .await()

            for (document in result) {
                units.add(document.toObject<WarehouseUnit>())
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error getting documents: ", e)
        }
        return units
    }
}