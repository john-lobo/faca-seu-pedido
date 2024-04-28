package com.jlndev.productservice.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.baseservice.firebase.ConfigFirebase.CHILD_USERS
import com.jlndev.baseservice.firebase.ConfigFirebase.COLLECTION_CART
import com.jlndev.baseservice.firebase.ConfigFirebase.COLLECTION_TOTAL
import com.jlndev.productservice.data.remote.model.Cart
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Single

class CartRemoteRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CartRepository {

    override fun getProductsItems(): Single<Cart> {
        val cartCollection =
            getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))

        return Single.create { emitter ->
            cartCollection.get()
                .addOnSuccessListener { result ->
                    val cart = result.toObject(Cart::class.java)
                    emitter.onSuccess(cart ?: Cart())
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun insertProductItem(product: ProductItemModel): Single<Cart> {
        val cartDocument = getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))

        return Single.create { emitter ->
            cartDocument.get()
                .addOnSuccessListener { result ->
                    val cart = result.toObject(Cart::class.java) ?: Cart()
                    val updatedProducts = cart.productItems.toMutableList()
                    val existingProductIndex = updatedProducts.indexOfFirst { it.id == product.id }

                    if (existingProductIndex != -1) {
                        updatedProducts[existingProductIndex] = product.copy(quantity = updatedProducts[existingProductIndex].quantity + product.quantity)
                    } else {
                        updatedProducts.add(product)
                    }

                    val totalQuantity = updatedProducts.sumOf { it.quantity }
                    val totalPrice = updatedProducts.sumOf { it.price * it.quantity }
                    val updatedCart = Cart(updatedProducts, totalQuantity, totalPrice)

                    cartDocument.set(updatedCart)
                        .addOnSuccessListener {
                            emitter.onSuccess(updatedCart)
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

    override fun deleteProductItem(product: ProductItemModel): Single<Cart> {
        val cartCollection = getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))

        return Single.create { emitter ->
            cartCollection.get()
                .addOnSuccessListener { result ->
                    val cart = result.toObject(Cart::class.java) ?: Cart()

                    val updatedProducts = cart.productItems.toMutableList()
                    val removed = updatedProducts.remove(product)

                    if (removed) {
                        val totalQuantity = cart.totalQuantity - product.quantity
                        val totalPrice = cart.totalPrice - (product.price * product.quantity)
                        val updatedCart = Cart(updatedProducts, totalQuantity, totalPrice)

                        cartCollection.set(updatedCart)
                            .addOnSuccessListener {
                                emitter.onSuccess(updatedCart)
                            }
                            .addOnFailureListener { exception ->
                                emitter.onError(exception)
                            }
                    } else {
                        emitter.onSuccess(cart)
                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }


    override fun deleteAllProductsItems(): Single<Cart> {
        val cartDocument = getUserCartCollectionRef() ?: return Single.error(Throwable("User not authenticated"))

        return Single.create { emitter ->
            cartDocument.get()
                .addOnSuccessListener {
                    val updatedCart = Cart()
                    cartDocument.set(updatedCart)
                        .addOnSuccessListener {
                            emitter.onSuccess(updatedCart)
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

    private fun getUserCartCollectionRef(): DocumentReference? {
        val userId = auth.currentUser?.uid
        return if (userId != null) {
            firestore
                .collection(CHILD_USERS)
                .document(userId)
                .collection(COLLECTION_CART)
                .document(COLLECTION_CART)
        } else {
            null
        }
    }

    private fun getUserTotalCollectionRef(): CollectionReference? {
        val userId = auth.currentUser?.uid
        return if (userId != null) {
            firestore
                .collection(CHILD_USERS)
                .document(userId)
                .collection(COLLECTION_TOTAL)
        } else {
            null
        }
    }
}