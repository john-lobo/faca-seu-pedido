package com.jlndev.productservice.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.baseservice.firebase.ConfigFirebase.CHILD_TOTAL
import com.jlndev.baseservice.firebase.ConfigFirebase.CHILD_USERS
import com.jlndev.baseservice.firebase.ConfigFirebase.COLLECTION_CART
import com.jlndev.baseservice.firebase.ConfigFirebase.COLLECTION_TOTAL
import com.jlndev.productservice.data.repository.model.CartTotalQuantity
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Single

class CartRemoteRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CartRepository {

    override fun getProductsItems(): Single<List<ProductItemModel>> {
        val cartCollection =
            getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))

        return Single.create { emitter ->
            cartCollection.get()
                .addOnSuccessListener { result ->
                    val products = mutableListOf<ProductItemModel>()
                    result.forEach { document ->
                        val productItemModel = document.toObject(ProductItemModel::class.java)
                        products.add(productItemModel)
                    }
                    emitter.onSuccess(products)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun insertProductItem(productItemModel: ProductItemModel): Single<ProductItemModel> {
        val cartCollection =
            getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))
        val cartItemDocument = cartCollection.document(productItemModel.id.toString())

        return Single.create { emitter ->
            cartItemDocument.set(productItemModel)
                .addOnSuccessListener {
                    emitter.onSuccess(productItemModel)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun deleteProductItem(productItemModel: ProductItemModel): Single<ProductItemModel> {
        val cartCollection =
            getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))
        val cartItemDocument = cartCollection.document(productItemModel.id.toString())

        return Single.create { emitter ->
            cartItemDocument.delete()
                .addOnSuccessListener {
                    emitter.onSuccess(productItemModel)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun deleteAllProductsItems(): Single<List<ProductItemModel>> {
        val cartCollection =
            getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))

        return Single.create { emitter ->
            cartCollection.get()
                .addOnSuccessListener { result ->
                    val deleteTasks = mutableListOf<Task<Void>>()
                    for (document in result) {
                        val deleteTask = document.reference.delete()
                        deleteTasks.add(deleteTask)
                    }

                    Tasks.whenAll(deleteTasks)
                        .addOnSuccessListener {
                            emitter.onSuccess(listOf())
                        }
                        .addOnFailureListener { exception ->
                            emitter.onError(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getProductItem(productItemModel: ProductItemModel): Single<ProductItemModel> {
        val cartCollection =
            getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))
        val cartItemDocument = cartCollection.document(productItemModel.id.toString())

        return Single.create { emitter ->
            cartItemDocument.set(productItemModel)
                .addOnSuccessListener {
                    emitter.onSuccess(productItemModel)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun updateTotalAfterOperation(): Single<CartTotalQuantity> {
        return getProductsItems()
            .flatMap { cartItems ->
                updateTotalCollection(cartItems)
            }
            .onErrorResumeNext {
                Single.error(it)
            }
    }

    private fun updateTotalCollection(cartItems: List<ProductItemModel>): Single<CartTotalQuantity> {
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            return Single.error(Throwable("User not authenticated"))
        }

        val totalPrice = cartItems.sumOf { it.price * it.quantity }
        val totalQuantity = cartItems.sumOf { it.quantity }

        val totalData = CartTotalQuantity(
            totalQuantity,
            totalPrice
        )

        return Single.create { emitter ->
            firestore
                .collection(CHILD_USERS)
                .document(userId)
                .collection(COLLECTION_TOTAL)
                .document(CHILD_TOTAL)
                .set(totalData)
                .addOnSuccessListener {
                    emitter.onSuccess(totalData)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    private fun getUserCartCollectionRef(): CollectionReference? {
        val userId = auth.currentUser?.uid
        return if (userId != null) {
            firestore
                .collection(CHILD_USERS)
                .document(userId)
                .collection(COLLECTION_CART)
        } else {
            null
        }
    }
}