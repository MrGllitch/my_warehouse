package eu.glitch.mywarehouse.database.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore, private val auth: FirebaseAuth
) : IRepository {

    var isInitialized = false

    private lateinit var dao: DocumentReference

    init {
        CoroutineScope(Dispatchers.IO).launch {
            updateDaoForNewCompany()
        }
    }
    override suspend fun isInitialized(): Boolean {
        if (!isInitialized) {
            updateDaoForNewCompany()
            isInitialized = true
        }
        return true
    }

    suspend fun updateDaoForNewCompany() {
        val userOrCompanyId = getUserOrCompanyId()
        dao = firestore.collection("users").document(userOrCompanyId)
    }

    private suspend fun getUserOrCompanyId(): String {
        val currentUser = auth.currentUser
        val userDocument = firestore.collection("users").document(currentUser?.uid ?: "")
        val user = userDocument.get().await().toObject(User::class.java)

        return user?.companyId ?: user?.id ?: currentUser?.uid
        ?: throw IllegalStateException("User not authenticated or missing company ID")
    }

    suspend fun fetchCurrentUserRole() : String {
        val currentUser = auth.currentUser
        val userDocument = firestore.collection("users").document(currentUser?.uid ?: "")
        val user = userDocument.get().await().toObject(User::class.java)

        val role = user?.role ?: "admin"
        return role
    }




    override suspend fun getProducts(): List<Product> {
        return getItems("products", Product::class.java)
    }

    override suspend fun insertProduct(product: Product): String {
        var id = product.id
        if (product.id == "") {
            val documentRef = dao.collection("products").add(product).await()
            id = documentRef.id
        } else {
            dao.collection("products").document(product.id).set(product).await()
        }
        return id
    }

    override suspend fun updateProduct(product: Product) {
        Log.e("update product", "updateProduct: calling $product", )
        dao.collection("products").document(product.id).set(product).await()
    }

    override suspend fun deleteProduct(product: Product) {
        dao.collection("products").document(product.id).delete().await()
        deleteRecordsForProduct(product.id)
        deleteProductsCrossRefs(product.id)
    }
    private suspend fun deleteProductDocumentId(documentId: String) {
        dao.collection("products").document(documentId).delete().await()
        deleteRecordsForProduct(documentId)
        deleteProductsCrossRefs(documentId)
    }

    override suspend fun getProduct(id: String): Product {
        return getItem(id, "products", Product::class.java)
    }

    override suspend fun insertProductChipCrossRef(crossRef: ProductChipCrossRef) {
        dao.collection("product_chip_cross_refs").add(crossRef).await()
    }

    override suspend fun deleteProductChipCrossRef(crossRef: ProductChipCrossRef) {
        val querySnapshot =
            dao.collection("product_chip_cross_refs").whereEqualTo("productId", crossRef.productId)
                .whereEqualTo("chipId", crossRef.chipId).get().await()

        for (document in querySnapshot.documents) {
            dao.collection("product_chip_cross_refs").document(document.id).delete().await()
        }
    }

    override suspend fun updateProductWithChips(product: Product, newChipIds: List<String>) {
        val oldChipIds = getChipIdsForProduct(product.id)

        // Delete removed chip cross-references
        val removedChipIds = oldChipIds - newChipIds
        for (chipId in removedChipIds) {
            val crossRef = ProductChipCrossRef(product.id, chipId)
            deleteProductChipCrossRef(crossRef)
        }

        // Add new chip cross-references
        val addedChipIds = newChipIds - oldChipIds
        for (chipId in addedChipIds) {
            val crossRef = ProductChipCrossRef(product.id, chipId)
            insertProductChipCrossRef(crossRef)
        }

        // Update the product itself
        updateProduct(product)
    }

    override suspend fun getChipIdsForProduct(productId: String): List<String> {
        val chipIds = mutableListOf<String>()
        val querySnapshot =
            dao.collection("product_chip_cross_refs").whereEqualTo("productId", productId).get()
                .await()

        for (document in querySnapshot.documents) {
            val crossRef = document.toObject(ProductChipCrossRef::class.java)
            crossRef?.chipId?.let { chipIds.add(it) }
        }
        return chipIds
    }

    override suspend fun getRecords(): List<Record> {
        return getItems("records", Record::class.java)
    }

    override suspend fun insertRecord(record: Record) {
        if (record.id == "") {
            dao.collection("records").add(record).await()
        } else {
            dao.collection("records").document(record.id).set(record).await()
        }
    }

    override suspend fun updateRecord(record: Record) {
        dao.collection("records").document(record.id).set(record).await()
    }

    override suspend fun deleteRecord(record: Record) {
        dao.collection("records").document(record.id).delete().await()
    }
    private suspend fun deleteRecordsForProduct(id: String) {
        var records = dao.collection("records").whereEqualTo("productId", id).get().await()
        records.forEach {
            dao.collection("records").document(it.id).delete().await()
        }
    }

    override suspend fun getRecord(id: String): Record {
        return getItem(id, "records", Record::class.java)
    }

    override suspend fun getRecordListItems(): List<RecordListItem> {
        var recordsListItems : MutableList<RecordListItem> = mutableListOf()
        val records : List<Record> = getRecords()
        records.forEach {
            val product = getProduct(it.productId)
            recordsListItems.add(RecordListItem(it,product.name,getUnit(product.unitId).shortcut, product.typeId, getChipIdsForProduct(product.id),getType(product.typeId).iconRoute))

        }
        return recordsListItems
    }

    override suspend fun insertUnit(unit: WarehouseUnit) {
        if (unit.id == "") {
            dao.collection("units").add(unit).await()
        } else {
            dao.collection("units").document(unit.id).set(unit).await()
        }
    }

    override suspend fun getUnits(): List<WarehouseUnit> {
        return getItems("units", WarehouseUnit::class.java)
    }

    override suspend fun getUnitsSize(): Int {
        val querySnapshot = dao.collection("units").get().await()

        return querySnapshot.size()
    }

    override suspend fun getUnit(id: String): WarehouseUnit {
        return getItem(id, "units", WarehouseUnit::class.java)
    }

    override suspend fun updateUnit(unit: WarehouseUnit) {
        dao.collection("units").document(unit.id).set(unit).await()
    }

    override suspend fun deleteUnit(unit: WarehouseUnit) {
        dao.collection("units").document(unit.id).delete().await()
        var products = dao.collection("products").whereEqualTo("unitId",unit.id).get().await()
        products.forEach { deleteProductDocumentId(it.id) }
    }

    override suspend fun getTypes(): List<Type> {
        return getItems("types", Type::class.java)
    }

    override suspend fun insertType(type: Type) {
        if (type.id == "") {
            dao.collection("types").add(type).await()
        } else {
            dao.collection("types").document(type.id).set(type).await()
        }
    }

    override suspend fun deleteType(type: Type) {
        dao.collection("types").document(type.id).delete().await()
        var products = dao.collection("products").whereEqualTo("typeId",type.id).get().await()
        products.forEach { deleteProductDocumentId(it.id) }
    }

    override suspend fun updateType(type: Type) {
        dao.collection("types").document(type.id).set(type).await()
    }

    override suspend fun getType(id: String): Type {
        return getItem(id, "types", Type::class.java)
    }

    override suspend fun getChips(): List<Chip> {
        return getItems("chips", Chip::class.java)
    }

    override suspend fun insertChip(chip: Chip): String {
        var id = chip.id
        if (chip.id == "") {
            val documentReference = dao.collection("chips").add(chip).await()
            id = documentReference.id
        } else {
            dao.collection("chips").document(chip.id).set(chip).await()
        }
        return id
    }

    override suspend fun updateChip(chip: Chip) {
        dao.collection("chips").document(chip.id).set(chip).await()
    }

    override suspend fun deleteChip(chip: Chip) {
        dao.collection("chips").document(chip.id).delete().await()
        deleteChipsCrossRefs(chip.id)
    }
    private suspend fun deleteChipsCrossRefs(chipId: String){
        var records = dao.collection("product_chip_cross_refs").whereEqualTo("chipId", chipId).get().await()
        records.forEach {
            dao.collection("product_chip_cross_refs").document(it.id).delete()
        }
    }
    private suspend fun deleteProductsCrossRefs(productId: String){
        var records = dao.collection("product_chip_cross_refs").whereEqualTo("productId", productId).get().await()
        records.forEach {
            dao.collection("product_chip_cross_refs").document(it.id).delete()
        }
    }

    override suspend fun getChip(id: String): Chip {
        return getItem(id, "chips", Chip::class.java)
    }

    private suspend fun <T> getItems(collection: String, clazz: Class<T>): List<T> {
        val items = mutableListOf<T>()
        try {
            val querySnapshot = dao.collection(collection).get().await()
            for (document in querySnapshot.documents) {
                val item = document.toObject(clazz)
                if (item != null) {
                    val itemIdField = clazz.getDeclaredField("id")
                    itemIdField.isAccessible = true
                    itemIdField.set(item, document.id)
                    items.add(item)
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error getting documents: ", e)
            // TODO: Throwing a custom exception or handling the error differently
        }
        return items
    }

    private suspend fun <T> getItem(id: String, collection: String, clazz: Class<T>): T {
        return try {
            val docSnapshot = dao.collection(collection).document(id).get().await()
            if (docSnapshot.exists()) {
                val item = docSnapshot.toObject(clazz)!!

                // Set the document ID to the `id` field of the object
                val itemIdField = clazz.getDeclaredField("id")
                itemIdField.isAccessible = true
                itemIdField.set(item, docSnapshot.id)

                item
            } else {
                throw IllegalArgumentException("Item with ID $id not found of class ${clazz.name}")
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error getting documents: ", e)
            throw e
            // TODO: Throwing a custom exception or handling the error differently
        }
    }

}
