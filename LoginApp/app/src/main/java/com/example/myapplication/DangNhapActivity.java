package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.LayoutDangNhapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangNhapActivity extends AppCompatActivity {
    LayoutDangNhapBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutDangNhapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = binding.edDNEmail.getText().toString();
                String strMatKhau = binding.edDNMatKhau.getText().toString();

                if(TextUtils.isEmpty(strEmail)){
                    binding.edDNEmail.setError("Vui lòng nhập email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    binding.edDNEmail.setError("Vui lòng nhập email đúng định dạng");
                } else if (TextUtils.isEmpty(strMatKhau)) {
                    binding.edDNMatKhau.setError("Vui lòng nhập mật khẩu");
                }else {
                    funcDangNhap(strEmail, strMatKhau);
                }
            }
        });

        binding.tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        binding.tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, QuenMatKhauActivity.class);
                startActivity(intent);
            }
        });

    }

    private void funcDangNhap(String email, String matKhau) {
        firebaseAuth.signInWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại. Email hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}