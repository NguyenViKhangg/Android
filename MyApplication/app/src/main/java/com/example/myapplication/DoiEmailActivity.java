package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.databinding.LayoutDoiEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiEmailActivity extends AppCompatActivity {
    LayoutDoiEmailBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    String strMailHienTai, strMailMoi, strMatKhau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutDoiEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressBar = binding.progressBar;
        getSupportActionBar().setTitle("Đổi email");

        binding.btnDoiEmail.setEnabled(false);
        binding.edDoiEmailEmailMoi.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        strMailHienTai = firebaseUser.getEmail();
        binding.edDoiEmailEmailHienTai.setText(strMailHienTai);

        if(firebaseUser.equals("")){
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
        }else {
            funcXacNhanTaiKhoan(firebaseUser);
        }
    }

    private void funcXacNhanTaiKhoan(FirebaseUser firebaseUser) {
        binding.btnDoiEmailXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strMatKhau = binding.edDoiEmailMatKhau.getText().toString();

                if(TextUtils.isEmpty(strMatKhau)){
                    binding.edDoiEmailMatKhau.setError("Vui lòng nhập mật khẩu");
                }else {
                    progressBar.setVisibility(view.VISIBLE);
                    AuthCredential authCredential = EmailAuthProvider.getCredential(strMailHienTai, strMatKhau);
                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                binding.tvDoiEmailHead2.setText("Tài khoản đã xác nhận. Bạn có thể đổi email ngay bây giờ.");

                                binding.btnDoiEmailXacNhan.setEnabled(false);
                                binding.edDoiEmailEmailHienTai.setEnabled(false);
                                binding.edDoiEmailMatKhau.setEnabled(false);
                                binding.btnDoiEmail.setEnabled(true);
                                binding.edDoiEmailEmailMoi.setEnabled(true);
                                binding.btnDoiEmail.setBackgroundTintList(ContextCompat.getColorStateList(DoiEmailActivity.this, R.color.dark_green));

                                binding.btnDoiEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        strMailMoi = binding.edDoiEmailEmailMoi.getText().toString();

                                        if (TextUtils.isEmpty(strMailMoi)){
                                            binding.edDoiEmailEmailMoi.setError("Vui lòng nhập email");
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(strMailMoi).matches()) {
                                            binding.edDoiEmailEmailMoi.setError("Vui lòng nhập lại email");
                                        } else if (strMailMoi.matches(strMailHienTai)) {
                                            binding.edDoiEmailEmailMoi.setError("Email mới phải khác email hiện tại");
                                        }else {
                                            progressBar.setVisibility(View.VISIBLE);
                                            funcCatNhatEmail(firebaseUser);
                                        }
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(DoiEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void funcCatNhatEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(strMailMoi).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
//                    firebaseUser.sendEmailVerification();
                    Toast.makeText(DoiEmailActivity.this, "Email đã được thay đổi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DoiEmailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(DoiEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}