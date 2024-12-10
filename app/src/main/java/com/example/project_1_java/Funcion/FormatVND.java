package com.example.project_1_java.Funcion;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatVND {
    public static String formatCurrency(String input) {
        String cleanString = input.replaceAll("[,.đ₫\\s]", "");
        if (cleanString.isEmpty()) {
            return "";
        }
        Float parsed = null;
        try {
            parsed = Float.parseFloat(cleanString);
        } catch (NumberFormatException e) {
            return "";
        }
        String formatted = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(parsed);
        String result = "đ" + formatted.replaceAll("\\s", "").replace("₫", "");
        return result;
    }

    public static float convertStringToFloat(String input) {
        if (input == null || input.isEmpty()) {
            return 0.0F;
        }
        String cleanInput = input.replace("đ", "");
        if (cleanInput.contains(",") && cleanInput.contains(".")) {
            cleanInput = cleanInput.replace(".", "").replace(",", ".");
        } else if (cleanInput.contains(",")) {
            cleanInput = cleanInput.replace(",", ".");
        }
        int lastDotIndex = cleanInput.lastIndexOf('.');
        if (lastDotIndex != -1) {
            cleanInput = cleanInput.substring(0, lastDotIndex).replace(".", "") + cleanInput.substring(lastDotIndex);
        }
        try {
            return Float.parseFloat(cleanInput);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0F;
        }
    }
}
