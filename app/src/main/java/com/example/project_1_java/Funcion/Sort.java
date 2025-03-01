package com.example.project_1_java.Funcion;

import com.example.project_1_java.Model.ModelProduct;

import java.util.ArrayList;
import java.util.List;

public class Sort {
    public void mergeSort(List<ModelProduct> list, int left, int right,boolean sort) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid,sort);
            mergeSort(list, mid + 1, right,sort);
            merge(list, left, mid, right,sort);
        }
    }
    private void merge(List<ModelProduct> list, int left, int mid, int right,boolean sort) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        List<ModelProduct> leftList = new ArrayList<>(list.subList(left, mid + 1));
        List<ModelProduct> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            float priceLeft = parsePrice(leftList.get(i).getPrice());
            float priceRight = parsePrice(rightList.get(j).getPrice());
            if ((sort&&priceLeft<=priceRight)||(!sort&&priceLeft>=priceRight)) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }
        while (i < n1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }
    private Float parsePrice(String price) {
        try {
            return FormatVND.convertStringToFloat(price);
        } catch (NumberFormatException e) {
            return 0F;
        }
    }
}
