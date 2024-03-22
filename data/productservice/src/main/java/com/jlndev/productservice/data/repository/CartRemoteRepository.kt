package com.jlndev.productservice.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.productservice.data.remote.model.UserCart
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single

class CartRemoteRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CartRepository {
    override fun getProductsItems(): Single<List<ProductItemModel>> {
            val userId = auth.currentUser?.uid

            if (userId.isNullOrEmpty()) {
                return Single.error(Throwable("User not authenticated"))
            }

            return Single.create { emitter ->
                firestore
                    .collection("users")
                    .document(userId)
                    .collection("cart")
                    .get()
                    .addOnSuccessListener { result ->
                        val products = mutableListOf<ProductItemModel>()
                        for (document in result) {
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
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            return Single.error(Throwable("User not authenticated"))
        }

        val cartItemDocument = firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(productItemModel.id.toString())

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
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            return Single.error(Throwable("User not authenticated"))
        }

        val cartItemDocument = firestore.collection("users").document(userId)
            .collection("cart").document(productItemModel.id.toString())

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
            val userId = auth.currentUser?.uid
            if (userId.isNullOrEmpty()) {
                return Single.error(Throwable("User not authenticated"))
            }

            val cartCollection = firestore.collection("users").document(userId)
                .collection("cart")

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
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            return Single.error(Throwable("User not authenticated"))
        }

        val cartItemDocument = firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(productItemModel.id.toString())

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

}
