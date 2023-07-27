package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.LayoutQuenMatKhauBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class QuenMatKhauActivity extends AppCompatActivity {
    private static final String TAG = "QuenMatKhauActivity";
    LayoutQuenMatKhauBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutQuenMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Quên mật khẩu");

        binding.btnDangNhapQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = binding.edQMKEmail.getText().toString();

                if(TextUtils.isEmpty(strEmail)){
                    binding.edQMKEmail.setError("Vui lòng nhập email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    binding.edQMKEmail.setError("Vui lòng nhập lại email");
                }else {
                    funcQuenMK(strEmail);
                }
            }
        });
    }

    private void funcQuenMK(String strEmail) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(strEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(QuenMatKhauActivity.this, "Kiểm tra email để nhận đường dẫn cập nhật lại mật khẩu", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(QuenMatKhauActivity.this, DangNhapActivity.class);
                    // Clear stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        binding.edQMKEmail.setError("Người dùng không tồn tại. Vui lòng kiểm tra lại");
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(QuenMatKhauActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}