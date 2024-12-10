package com.example.project_1_java.Store.Classify;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.Funcion.HideKeyboard;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.InterFace.ClassifyData;
import com.example.project_1_java.databinding.FragmentClassifyInputBinding;

public class ClassifyInputFragment extends Fragment {
    private ClassifyData data;
    private HideKeyboard hideKeyboard;
    private FragmentClassifyInputBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClassifyInputBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideKeyboard = new HideKeyboard(requireActivity());
        setupListener();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof ClassifyData ){
            data = (ClassifyData)  getParentFragment();
        }
    }

    private void setupListener() {
        binding.btnOff.setOnClickListener(v-> getParentFragmentManager().popBackStack());
        binding.edtInputPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s==null || s.toString().isEmpty()) return;
                binding.edtInputPrice.removeTextChangedListener(this);
                String originalString = s.toString();
                String format = FormatVND.formatCurrency(originalString);
                binding.edtInputPrice.setText(format);
                binding.edtInputPrice.setSelection(format.length());
                binding.edtInputPrice.addTextChangedListener(this);
            }
        });
        binding.edtInputTitle.requestFocus();
        binding.edtInputPrice.setOnEditorActionListener((v, actionId, event)->{
            if (actionId == EditorInfo.IME_ACTION_DONE){
                String inputTitle = binding.edtInputTitle.getText().toString().trim();
                String inputPrice = binding.edtInputPrice.getText().toString().trim();
                if (inputTitle.isEmpty()){
                    binding.edtInputTitle.setBackgroundResource(R.drawable.red_border);
                    return false;
                }else if (inputPrice.isEmpty()){
                    binding.edtInputPrice.setBackgroundResource(R.drawable.red_border);
                    return false;
                }
                if (data != null) {
                    data.onDataReceived(inputTitle, inputPrice);
                }
                binding.edtInputTitle.setText("");
                binding.edtInputPrice.setText("");
                hideKeyboard.hide();
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
                return true;
            }
            return false;
        });
    }
}