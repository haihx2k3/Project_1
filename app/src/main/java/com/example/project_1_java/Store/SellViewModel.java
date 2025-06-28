package com.example.project_1_java.Store;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.Model.ImageModel;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SellViewModel extends ViewModel {
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private StorageReference strRef = FirebaseStorage.getInstance().getReference("Images");
    private MutableLiveData<Boolean> _dataSaved = new MutableLiveData<>();
    public LiveData<Boolean> getDataSaved() {
        return _dataSaved;
    }
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    //Upload product to Firebase
    public void saveDataSell(String title,String detail, String price, List<Uri> listUri, List<ClassifyModel> classify,String branch) {
        executorService.execute(() -> {
            try {
                String id = dbRef.push().getKey();
                List<byte[]> compressedImages = fetchAndCompressImages(listUri);
                List<String> productImage = uploadImagesToFirebase(compressedImages, id);
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String sellerId = currentUser.getUid();
                    List<ClassifyModel> classifyImage = uploadVariantImages(classify, id);
                    ModelProduct product = new ModelProduct(id, sellerId, title, detail, price, productImage, classifyImage);
                    new FirebaseUtil().addProduct(id, branch,product , new Runnable() {
                        @Override
                        public void run() {
                            _dataSaved.postValue(true);
                            executorService.shutdown();
                        }
                    }, new FirebaseUtil.FailureCallback() {
                        @Override
                        public void onFailure(String errorMessage) {
                            _dataSaved.postValue(false);
                            executorService.shutdown();
                        }
                    });
                }
            } catch (Exception e) {
                _dataSaved.postValue(false);
                e.printStackTrace();
            }
        });
    }
    //Save info user to Firebase
    public void updateUser(String name, String gender, String birthDay, Uri avatar) {
        String id = dbRef.push().getKey();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                byte[] compressedImages = fetchAndCompressUser(avatar);
                String productImage = uploadImageUser(compressedImages, id);
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    new FirebaseUtil().updateProfile(userId, name, birthDay, gender, productImage, () -> _dataSaved.postValue(true));
                }
            } catch (Exception e) {
                _dataSaved.postValue(false);
                e.printStackTrace();
            }
        });
    }
    // Trong SellViewModel.java
//    public void addAds(List<Uri> listUri) {
//        executorService.execute(() -> {
//            try {
//                String id = dbRef.push().getKey(); // Tạo ID duy nhất cho quảng cáo
//                List<byte[]> compressedImages = fetchAndCompressImages(listUri); // Nén hình ảnh
//                List<String> imageUrls = uploadImagesToFirebase(compressedImages, id); // Tải hình lên Firebase Storage
//                ImageModel imageModel = new ImageModel(id, imageUrls); // Tạo đối tượng ImageModel
//                new FirebaseUtil().addAds(id, imageModel, () -> {
//                    _dataSaved.postValue(true); // Cập nhật LiveData khi thành công
//                    executorService.shutdown();
//                }, errorMessage -> {
//                    _dataSaved.postValue(false); // Cập nhật LiveData khi thất bại
//                    executorService.shutdown();
//                });
//            } catch (Exception e) {
//                _dataSaved.postValue(false);
//                e.printStackTrace();
//            }
//        });
//    }
    //User avatar processing
    private byte[] fetchAndCompressUser(Uri uri) throws Exception {
        return Executors.newSingleThreadExecutor().submit(() -> {
            Bitmap bitmap = Picasso.get().load(uri).get();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            return baos.toByteArray();
        }).get();
    }
    //Upload avatar to Firebase
    private String uploadImageUser(byte[] imageData, String id) throws Exception {
        return Executors.newSingleThreadExecutor().submit(() -> {
            StorageReference imageRef = strRef.child(id + ".jpg");
            Tasks.await(imageRef.putBytes(imageData));
            return Tasks.await(imageRef.getDownloadUrl()).toString();
        }).get();
    }
    //Product image processing
    private List<byte[]> fetchAndCompressImages(List<Uri> uris) throws Exception {
        return Executors.newSingleThreadExecutor().submit(() -> {
            List<byte[]> compressedImages = new ArrayList<>();
            for (Uri uri : uris) {
                Bitmap bitmap = Picasso.get().load(uri).get();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                compressedImages.add(baos.toByteArray());
            }
            return compressedImages;
        }).get();
    }
    //Upload product image to Firebase
    private List<String> uploadImagesToFirebase(List<byte[]> images, String id) throws Exception {
        return Executors.newSingleThreadExecutor().submit(() -> {
            List<String> imageUrls = new ArrayList<>();
            for (int index = 0; index < images.size(); index++) {
                byte[] imageData = images.get(index);
                StorageReference imageRef = strRef.child(id + "/" + index + ".jpg");
                Tasks.await(imageRef.putBytes(imageData));
                String imageUrl = Tasks.await(imageRef.getDownloadUrl()).toString();
                imageUrls.add(imageUrl);
            }
            return imageUrls;
        }).get();
    }
    //Upload data array category
    private List<ClassifyModel> uploadVariantImages(List<ClassifyModel> variants, String id) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> {
                List<ClassifyModel> updatedVariants = new ArrayList<>();
                for (ClassifyModel variant : variants) {
                    byte[] imageData = fetchAndCompressImages(Collections.singletonList(Uri.parse(variant.getImg()))).get(0);
                    StorageReference imageRef = strRef.child(id + "/" + variant.getTitle() + ".jpg");
                    Tasks.await(imageRef.putBytes(imageData));
                    String imageUrl = Tasks.await(imageRef.getDownloadUrl()).toString();
                    updatedVariants.add(variant.copy(imageUrl));
                }
                return updatedVariants;
            }).get();
        } catch (Exception e) {
            Log.e("SellViewModel", "Error uploading variant images: " + e.getMessage(), e);
            throw e;
        } finally {
            executor.shutdown();
        }
    }
}
