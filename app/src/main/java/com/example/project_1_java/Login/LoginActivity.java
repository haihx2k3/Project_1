package com.example.project_1_java.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.project_1_java.Footer.SecondHomeFragment;
import com.example.project_1_java.Funcion.LoadingEffect;
import com.example.project_1_java.InterFace.OnLogIn;
import com.example.project_1_java.InterFace.OnLogOut;
import com.example.project_1_java.Model.UserModel;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.example.project_1_java.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private CountryCodePicker countryCodePicker;
    private UserModel userModel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private long timeOutSecond = 60L;
    private String verificationCode = "";
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userModel = new UserModel();
        setupListener();
    }

    private void setupListener() {
        countryCodePicker = binding.countryCodePicker;
        countryCodePicker.registerCarrierNumberEditText(binding.edtPhone);
        String[] phone = {null};

        binding.btnOtp.setOnClickListener(v -> {
            if (!countryCodePicker.isValidFullNumber()) {
                binding.edtPhone.setError("Số điện thoại không chính xác");
                return;
            }
            binding.txtResent.setVisibility(View.VISIBLE);
            phone[0] = countryCodePicker.getFullNumberWithPlus();
            sendOtp(phone[0], false);
            checkIsEnable();
        });

        binding.btnLoginPhone.setOnClickListener(v -> {
            String otp = binding.edtOTP.getText().toString();
            if (verificationCode.isEmpty()) {
                return;
            }
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
            signIn(credential, phone[0]);
        });

        binding.txtResent.setOnClickListener(v -> {
            sendOtp(phone[0], true);
            checkIsEnable();
        });
    }

    private void checkIsEnable() {
        binding.btnOtp.setEnabled(false);
        binding.btnOtp.setTextColor(Color.GRAY);
        binding.edtPhone.setEnabled(false);
    }

    private void sendOtp(String phone, boolean isResend) {
        startResendTimer();
        onClickOtp();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        signIn(credential, phone);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("Error OTP", e.toString());
                    }

                    @Override
                    public void onCodeSent(@NonNull String p0, @NonNull PhoneAuthProvider.ForceResendingToken p1) {
                        super.onCodeSent(p0, p1);
                        verificationCode = p0;
                        resendingToken = p1;
                        Log.d("Resend", "Resent Complete");
                    }
                };

        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(timeOutSecond, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks);

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void startResendTimer() {
        binding.txtResent.setEnabled(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    timeOutSecond--;
                    binding.txtResent.setText("Resend OTP in " + timeOutSecond + " seconds");
                });
                if (timeOutSecond <= 0) {
                    timeOutSecond = 60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        binding.txtResent.setText("Resend OTP");
                        binding.btnOtp.setEnabled(true);
                        binding.btnOtp.setTextColor(Color.BLACK);
                        binding.txtResent.setEnabled(true);
                        binding.edtPhone.setEnabled(true);
                    });
                }
            }
        }, 0, 1000);
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void signIn(PhoneAuthCredential credential, String phone) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(userCheckTask -> {
                            if (userCheckTask.isSuccessful() && userCheckTask.getResult().exists()) {
                                successOtp();
                            } else {
                                userModel = new UserModel(FirebaseUtil.currentUserId(), generateRandomString(10), Timestamp.now(), "", "", "", phone);
                                FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        successOtp();
                                    }
                                });
                            }
                        });

                    } else {
                        Snackbar.make(this.getCurrentFocus(), "Xác nhận không thành công", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveButtonState(boolean hidden) {
        Context context = this;
        context.getSharedPreferences("saveLogin", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("hide", hidden)
                .apply();
    }

    private void onClickOtp() {
        binding.edtPhone.setVisibility(View.GONE);
        binding.edtOTP.setVisibility(View.VISIBLE);
        binding.btnOtp.setVisibility(View.GONE);
        binding.btnLoginPhone.setVisibility(View.VISIBLE);
    }

    private void successOtp() {
        LoadingEffect.showLoading(this,3000,()->{
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            saveButtonState(true);
            finish();
        });
    }
}