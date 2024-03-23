package com.jlndev.productservice.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.jlndev.baseservice.firebase.ConfigFirebase.CHILD_USERS
import com.jlndev.baseservice.firebase.ConfigFirebase.COLLECTION_ORDERS
import com.jlndev.productservice.data.db.dao.CartDao
import com.jlndev.productservice.data.db.dao.OrderHistoryDao
import com.jlndev.productservice.data.db.model.OrderHistoryEntity
import com.jlndev.productservice.data.remote.model.UserOrderHistory
import com.jlndev.productservice.data.repository.model.OrderHistoryItemModel
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single
import java.util.UUID

class OrderHistoryRemoteRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : OrderHistoryRepository {

    override fun getAllOrders(): Single<List<OrderHistoryItemModel>> {
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            return Single.error(Throwable("User not authenticated"))
        }

        val ordersCollectionRef = firestore.collection(CHILD_USERS).document(userId).collection(COLLECTION_ORDERS)

        return Single.create { emitter ->
            ordersCollectionRef.get()
                .addOnSuccessListener { result ->
                    val orders = mutableListOf<OrderHistoryItemModel>()
                    for (document in result) {
                        val orderHistory = document.toObject(UserOrderHistory::class.java)
                        val orderItems = Gson().fromJson(orderHistory.order, Array<ProductItemModel>::class.java).asList()
                        val orderHistoryItem = OrderHistoryItemModel(
                            orderHistory.id,
                            orderItems,
                            orderHistory.quantityItems,
                            orderHistory.totalValue
                        )
                        orders.add(orderHistoryItem)
                    }
                    emitter.onSuccess(orders)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun insertOrder(productsItems: List<ProductItemModel>): Completable {
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            return Completable.error(Throwable("User not authenticated"))
        }

        val orderCollectionRef = firestore.collection(CHILD_USERS).document(userId).collection(COLLECTION_ORDERS)

        val orderItem = Gson().toJson(productsItems)
        val totalValue = productsItems.sumOf { it.price * it.quantity }
        val quantityItems = productsItems.size
        val randomKey = UUID.randomUUID().toString()

        val orderData = UserOrderHistory(
            randomKey,
            orderItem,
            quantityItems,
            totalValue
        )

        return Completable.create { emitter ->
            orderCollectionRef.add(orderData)
                .addOnSuccessListener { documentReference ->
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }
}