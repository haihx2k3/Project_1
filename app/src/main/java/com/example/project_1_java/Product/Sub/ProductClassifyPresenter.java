package com.example.project_1_java.Product.Sub;

import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.Model.OrderModel;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductClassifyPresenter implements ProductClassifyConTract.Presenter{
    private final ProductClassifyConTract.View view;
    private final FirebaseUtil firebaseUtil;
    private final FormatVND frm;
    private final DatabaseReference dbr;
    private List<ClassifyModel> sub;
    private int sum;
    public ProductClassifyPresenter(ProductClassifyConTract.View view){
        this.view = view;
        this.firebaseUtil = new FirebaseUtil();
        this.frm = new FormatVND();
        this.dbr = FirebaseDatabase.getInstance().getReference();
        this.sub = new ArrayList<>();
        this.sum = 1;
    }
    @Override
    public void loadProductDetails(String id) {
        firebaseUtil.getSubProduct(id, new FirebaseUtil.ItemCallback<List<ClassifyModel>>() {
            @Override
            public void onResult(List<ClassifyModel> result) {
                sub.addAll(result);
                view.showClassifyList(result);
            }
        }, new FirebaseUtil.FailureCallback() {
            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void onAddToCartClicked(String title, String type, String price, String img, int count, String sellerId) {
        Float convert = frm.convertStringToFloat(price);
        Float totalPr = convert*sum;
        String idOrder = dbr.push().getKey();
        OrderModel order = new OrderModel(idOrder,title,type,price,img,sum,totalPr,sellerId);
        firebaseUtil.addItemToCard(idOrder,order,()-> view.navigateToCardActivity(),errorMessage -> view.showError(errorMessage));
    }

    @Override
    public void onPlusClicked() {
        sum++;
        view.updateSum(sum);
    }

    @Override
    public void onMinusClicked() {
        if (sum > 1) {
            sum--;
            view.updateSum(sum);
        }
    }
}
