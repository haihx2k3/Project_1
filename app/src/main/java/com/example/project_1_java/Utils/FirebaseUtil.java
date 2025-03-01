package com.example.project_1_java.Utils;

import android.util.Log;

import com.example.project_1_java.Model.AvailableModel;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.Model.ImageModel;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.Model.OrderModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUtil {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DocumentSnapshot lastData;
    private boolean isLoading = false;
    public static String currentUserId() {
       return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserDetails() {
       return FirebaseFirestore.getInstance().collection("Users").document(currentUserId());
    }

    public DocumentReference getChatRoomReference(String chatRoomId) {
        return FirebaseFirestore.getInstance().collection("chatRooms").document(chatRoomId);
    }
    public CollectionReference getChatRoomMessageReferences(String chatRoomId) {
        return getChatRoomReference(chatRoomId).collection("chats");
    }
    public String getChatRoomId(String userId1, String userId2) {
        return userId1.hashCode() < userId2.hashCode()
                ? userId1 + "_" + userId2
                : userId2 + "_" + userId1;
    }
    public static CollectionReference allChatRoomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatRooms");
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("Users");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userId){
        if (userId.get(0).equals(FirebaseUtil.currentUserId())){
            return allUserCollectionReference().document(userId.get(1));
        }else {
            return allUserCollectionReference().document(userId.get(0));
        }
    }
    public void getInfoUser( final Callback<Pair<String, String>> callback) {
        DocumentReference userDocRef = currentUserDetails();
        if (userDocRef==null) {
            callback.onResult(new Pair<>(null, null));
            return;
        }

        userDocRef.get(Source.SERVER)
                .addOnSuccessListener(serverDocument -> {
                    if (serverDocument != null && serverDocument.exists()) {
                        String userName = serverDocument.getString("userName");
                        String avatar = serverDocument.getString("imgProfile");
                        callback.onResult(new Pair<>(userName, avatar));
                    } else {
                        callback.onResult(new Pair<>(null, null));
                    }
                })
                .addOnFailureListener(exception -> callback.onResult(new Pair<>(null, null)));
    }
    public void getInfoSeller(String sellerId, final Callback<Pair<String, String>> callback) {
        DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("Users").document(sellerId);
        if (userDocRef == null) {
            callback.onResult(new Pair<>(null, null));
            return;
        }

        userDocRef.get(Source.SERVER)
                .addOnSuccessListener(serverDocument -> {
                    if (serverDocument != null && serverDocument.exists()) {
                        String userName = serverDocument.getString("userName");
                        String avatar = serverDocument.getString("imgProfile");
                        callback.onResult(new Pair<>(userName, avatar));
                    } else {
                        callback.onResult(new Pair<>(null, null));
                    }
                })
                .addOnFailureListener(exception -> callback.onResult(new Pair<>(null, null)));
    }
    public void getProfile(final Callback<Pair<String, String>> callback) {
        DocumentReference userDocRef = currentUserDetails();
        userDocRef.get(Source.SERVER)
                .addOnSuccessListener(serverDocument -> {
                    if (serverDocument != null && serverDocument.exists()) {
                        String gender = serverDocument.getString("gender");
                        String birthday = serverDocument.getString("birthDay");
                        callback.onResult(new Pair<>(gender, birthday));
                    } else {
                        callback.onResult(new Pair<>(null, null));
                    }
                })
                .addOnFailureListener(exception -> callback.onResult(new Pair<>(null, null)));
    }


    public void addProduct(String id,String branch, ModelProduct item, Runnable onSuccess, FailureCallback onFailure) {
        List<Task<Void>> tasks = new ArrayList<>();
        tasks.add(firestore.collection("Product").document(id).set(item));
        tasks.add(firestore.collection("Booth").document(currentUserId()).collection("Available").document(id).set(item));
        tasks.add(firestore.collection(branch).document(id).set(item));

        Tasks.whenAll(tasks)
                .addOnSuccessListener(aVoid->onSuccess.run())
                .addOnFailureListener(e-> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error addProduct"));

    }

    public void addItemToCard( String id, OrderModel item, Runnable onSuccess, FailureCallback onFailure) {
        firestore.collection("Card").document(currentUserId()).collection("card").document(id).set(item)
                .addOnSuccessListener(aVoid -> onSuccess.run())
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error occurred"));
    }

    public void addItemOder(String sellerId, String idOder, OrderClassifyModel item, Runnable onSuccess, FailureCallback onFailure) {
        WriteBatch batch = firestore.batch();
        DocumentReference pendingRef = firestore.collection("Booth").document(sellerId).collection("Pending").document(idOder);
        DocumentReference statusRef = firestore.collection("OrderBuyer").document(currentUserId()).collection("status").document(idOder);
        DocumentReference cardRef = firestore.collection("Card").document(currentUserId()).collection("card").document(idOder);
        batch.set(pendingRef, item);
        batch.set(statusRef, item);
        batch.delete(cardRef);
        batch.commit()
                .addOnSuccessListener(aVoid -> onSuccess.run())
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error addItemOrder"));

    }
    public void addItemSending(String sellerId,String idOder, OrderClassifyModel item, Runnable onSuccess, FailureCallback onFailure) {
        WriteBatch batch = firestore.batch();
        DocumentReference sendingRef = firestore.collection("Booth").document(sellerId).collection("Sending").document(idOder);
        DocumentReference pendingRef = firestore.collection("Booth").document(sellerId).collection("Pending").document(idOder);
        batch.set(sendingRef, item);
        batch.update(sendingRef, "status","Sending");
        batch.delete(pendingRef);
        batch.commit()
                .addOnSuccessListener(aVoid -> onSuccess.run())
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error addItemOrder"));

    }
    public void addAds(String id, ImageModel image, Runnable onSuccess, FailureCallback onFailure) {
        firestore.collection("Ads").document(id).set(image)
                .addOnSuccessListener(aVoid -> onSuccess.run())
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error occurred"));
    }
    public void getItemProduct(ItemCallback<List<ModelProduct>> onSuccess, FailureCallback onFailure) {
        if (isLoading) return;
        isLoading = true;
        Query query = firestore.collection("Product").limit(10);
        if (lastData!=null){
            query = query.startAfter(lastData);
        }
        query.get().addOnSuccessListener(result->{
            List<ModelProduct> itemList = new ArrayList<>();
                    if (result != null && !result.getDocuments().isEmpty()) {
                        for (DocumentSnapshot document : result.getDocuments()) {
                            ModelProduct item = document.toObject(ModelProduct.class);
                            if (item != null) {
                                itemList.add(item);
                            }
                        }
                        lastData = result.getDocuments().get(result.size() - 1);
                    }
                    isLoading = false;
                    onSuccess.onResult(itemList);
                })
                .addOnFailureListener(e -> {
                    isLoading = false;
                    onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error getItemProduct");
                });
    }
    public void getAds(ItemCallback<List<String>> onSuccess, FailureCallback onFailure) {
        firestore.collection("Ads").get()
                .addOnSuccessListener(result -> {
                    List<String> itemList = new ArrayList<>();
                    if (result != null && !result.getDocuments().isEmpty()) {
                        for (DocumentSnapshot document : result.getDocuments()) {
                            ImageModel item = document.toObject(ImageModel.class);
                            if (item != null && item.getImage() != null) {
                                itemList.addAll(item.getImage());
                            }
                        }
                    }
                    onSuccess.onResult(itemList);
                })
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error occurred"));
    }

    public void getItemProductId(String id, ItemCallback<List<String>> onSuccess, FailureCallback onFailure) {
        firestore.collection("Product").document(id).get()
                .addOnSuccessListener(result -> {
                    if (result.exists()) {
                        List<String> uriList = (List<String>) result.get("uriList");
                        onSuccess.onResult(uriList);
                    }
                })
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error occurred"));
    }

    public void getSubProduct(String id, ItemCallback<List<ClassifyModel>> onSuccess, FailureCallback onFailure) {
        firestore.collection("Product").document(id).get()
                .addOnSuccessListener(result -> {
                    if (result.exists()) {
                        List<HashMap<String, Object>> subList = (List<HashMap<String, Object>>) result.get("classify");
                        ArrayList<ClassifyModel> sub = new ArrayList<>();
                        Gson gson = new Gson();
                        if (subList != null) {
                            for (HashMap<String, Object> map : subList) {
                                String json = gson.toJson(map);
                                ClassifyModel model = gson.fromJson(json, ClassifyModel.class);
                                sub.add(model);
                            }
                        }
                        onSuccess.onResult(sub);
                    }
                })
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error occurred"));
    }


    public void getItemToCard( ItemCallback<List<OrderModel>> onSuccess, FailureCallback onFailure) {
        firestore.collection("Card").document(currentUserId()).collection("card").get()
                .addOnSuccessListener(result -> {
                    List<OrderModel> itemList = new ArrayList<>();
                    for (DocumentSnapshot document : result.getDocuments()) {
                        OrderModel item = document.toObject(OrderModel.class);
                        itemList.add(item);
                    }
                    onSuccess.onResult(itemList);
                })
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error occurred get Card"));
    }

    public void getItemAvailable(ItemCallback<List<AvailableModel>> onSuccess, FailureCallback onFailure) {
        if (isLoading) return;
        isLoading = true;
        Query query = firestore.collection("Booth").document(currentUserId()).collection("Available").limit(10);
        if (lastData!=null){
            query = query.startAfter(lastData);
        }
        query.get().addOnSuccessListener(result->{
                    List<AvailableModel> itemList = new ArrayList<>();
                    if (result != null && !result.getDocuments().isEmpty()) {
                        for (DocumentSnapshot document : result.getDocuments()) {
                            AvailableModel item = document.toObject(AvailableModel.class);
                            if (item != null) {
                                itemList.add(item);
                            }
                        }
                        lastData = result.getDocuments().get(result.size() - 1);
                    }
                    isLoading = false;
                    onSuccess.onResult(itemList);
                })
                .addOnFailureListener(e -> {
                    isLoading = false;
                    onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error get item to Available");
                });
    }
    public void getItemPending(ItemCallback<List<OrderClassifyModel>> onSuccess, FailureCallback onFailure) {
        if (isLoading) return;
        isLoading = true;
        Query query = firestore.collection("Booth").document(currentUserId()).collection("Pending").limit(10);
        if (lastData!=null){
            query = query.startAfter(lastData);
        }
        query.get().addOnSuccessListener(result->{
                    List<OrderClassifyModel> itemList = new ArrayList<>();
                    if (result != null && !result.getDocuments().isEmpty()) {
                        for (DocumentSnapshot document : result.getDocuments()) {
                            OrderClassifyModel item = document.toObject(OrderClassifyModel.class);
                            if (item != null) {
                                itemList.add(item);
                            }
                        }
                        lastData = result.getDocuments().get(result.size() - 1);
                    }
                    isLoading = false;
                    onSuccess.onResult(itemList);
                })
                .addOnFailureListener(e -> {
                    isLoading = false;
                    onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error get item to Pending");
                });
    }
    public void getItemSending(ItemCallback<List<OrderClassifyModel>> onSuccess, FailureCallback onFailure) {
        if (isLoading) return;
        isLoading = true;
        Query query = firestore.collection("Booth").document(currentUserId()).collection("Sending").limit(10);
        if (lastData!=null){
            query = query.startAfter(lastData);
        }
        query.get().addOnSuccessListener(result->{
                    List<OrderClassifyModel> itemList = new ArrayList<>();
                    if (result != null && !result.getDocuments().isEmpty()) {
                        for (DocumentSnapshot document : result.getDocuments()) {
                            OrderClassifyModel item = document.toObject(OrderClassifyModel.class);
                            if (item != null) {
                                itemList.add(item);
                            }
                        }
                        lastData = result.getDocuments().get(result.size() - 1);
                    }
                    isLoading = false;
                    onSuccess.onResult(itemList);
                })
                .addOnFailureListener(e -> {
                    isLoading = false;
                    onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error get item to Sending");
                });
    }
    public void getCategory(String id,ItemCallback<List<ClassifyModel>> onSuccess,FailureCallback onFailure){
        firestore.collection("Booth").document(currentUserId()).collection("Available").document(id).get()
                .addOnSuccessListener(result -> {
                    if (result.exists()) {
                        List<HashMap<String, Object>> subList = (List<HashMap<String, Object>>) result.get("classify");
                        ArrayList<ClassifyModel> sub = new ArrayList<>();
                        Gson gson = new Gson();
                        if (subList != null) {
                            for (HashMap<String, Object> map : subList) {
                                String json = gson.toJson(map);
                                ClassifyModel model = gson.fromJson(json, ClassifyModel.class);
                                sub.add(model);
                            }
                        }
                        onSuccess.onResult(sub);
                    }
                })
                .addOnFailureListener(e -> onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error occurred"));
    }
    public void getBranchProduct(String branch,ItemCallback<List<ModelProduct>> onSuccess, FailureCallback onFailure) {
        if (isLoading) return;
        isLoading = true;
        Query query = firestore.collection(branch).limit(10);
        if (lastData!=null){
            query = query.startAfter(lastData);
        }
        List<ModelProduct> itemList = Collections.synchronizedList(new ArrayList<>());
        query.get().addOnSuccessListener(result->{
                    if (result != null && !result.getDocuments().isEmpty()) {
                        for (DocumentSnapshot document : result.getDocuments()) {
                            ModelProduct item = document.toObject(ModelProduct.class);
                            if (item != null) {
                                itemList.add(item);
                            }
                        }
                        lastData = result.getDocuments().get(result.size() - 1);
                    }
                    isLoading = false;
                    onSuccess.onResult(itemList);
                })
                .addOnFailureListener(e -> {
                    isLoading = false;
                    onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error get Branch");
                });
    }

    public void updateOder(String id, int count, Float total) {
        Map<String, Object> updates = Map.of("count" , count, "total", total);
        firestore.collection("Card").document(currentUserId()).collection("card").document(id).update(updates);
    }

    public void updateProfile(String user, String name, String birthday, String gender, String images, Runnable onSuccess) {
        Map<String, Object> updates = new HashMap<>();
        if (name != null) updates.put("userName", name);
        if (birthday != null) updates.put("birthDay", birthday);
        if (gender != null) updates.put("gender", gender);
        if (images != null) updates.put("imgProfile", images);
        if (!updates.isEmpty()) {
            firestore.collection("Users").document(user).update(updates).addOnSuccessListener(aVoid -> onSuccess.run());
        }
    }
    public void deleteOder (String id) {
        firestore.collection("Card").document(currentUserId()).collection("card").document(id).delete();
    }
    public void searchProduct(String searchKeyword,ItemCallback<List<ModelProduct>> onSuccess, FailureCallback onFailure){
        if (isLoading) return;
        isLoading = true;
        Query query = firestore.collection("Product").whereGreaterThanOrEqualTo("title",searchKeyword)
                .whereLessThanOrEqualTo("title",searchKeyword+"\uf8ff").limit(10);
        if (lastData!=null){
            query = query.startAfter(lastData);
        }
        List<ModelProduct> itemList = Collections.synchronizedList(new ArrayList<>());
        query.get().addOnSuccessListener(result->{
                    if (result != null && !result.getDocuments().isEmpty()) {
                        for (DocumentSnapshot document : result.getDocuments()) {
                            ModelProduct item = document.toObject(ModelProduct.class);
                            if (item != null) {
                                itemList.add(item);
                            }
                        }
                        lastData = result.getDocuments().get(result.size() - 1);
                    }
                    isLoading = false;
                    onSuccess.onResult(itemList);
                })
                .addOnFailureListener(e -> {
                    isLoading = false;
                    onFailure.onFailure(e.getMessage() != null ? e.getMessage() : "Error Search");
                });
    }

    // Callback interfaces for handling results
    public interface Callback<T> {
        void onResult(T result);
    }

    public interface FailureCallback {
        void onFailure(String errorMessage);
    }

    public interface ItemCallback<T> {
        void onResult(T result);
    }

    // Pair class to hold two values
    public static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
    }
}
