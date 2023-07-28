package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.databinding.LayoutDoiMatKhauBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhauActivity extends AppCompatActivity {
    LayoutDoiMatKhauBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    String strMKHienTai, strMKMoi, strMkNhapLai, strEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutDoiMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Đổi mật khẩu");
        progressBar = binding.progressBar;

        binding.btnDMK.setEnabled(false);
        binding.edDMKMatKhauMoi.setEnabled(false);
        binding.edDMKMatKhauNhapLai.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser.equals("")){
            Toast.makeText(DoiMatKhauActivity.this, "Lỗi !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DoiMatKhauActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            funcXacNhanTaiKhoan(firebaseUser);

        }
    }

    private void funcXacNhanTaiKhoan(FirebaseUser firebaseUser) {
        binding.btnDMKXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strMKHienTai = binding.edDMKMKHienTai.getText().toString();

                if(TextUtils.isEmpty(strMKHienTai)){
                    binding.edDMKMatKhauNhapLai.setError("Vui lòng nhập mật khẩu");
                }else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), strMKHienTai);
                    firebaseUser.reauthenticate((authCredential)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                binding.edDMKMKHienTai.setEnabled(false);
                                binding.btnDMKXacNhan.setEnabled(false);
                                binding.edDMKMatKhauMoi.setEnabled(true);
                                binding.edDMKMatKhauNhapLai.setEnabled(true);
                                binding.btnDMK.setEnabled(true);
                                binding.btnDMK.setBackgroundTintList(ContextCompat.getColorStateList(DoiMatKhauActivity.this, R.color.dark_green));
                                binding.tvDoiEmailHead2.setText("Tài khoản đã được xác nhận. Bạn có thể đổi mật khẩu ngay bây giờ");

                                Toast.makeText(DoiMatKhauActivity.this, "Xác nhận thành công", Toast.LENGTH_SHORT).show();

                                binding.btnDMK.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        funcDoiMatKhau(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(DoiMatKhauActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void funcDoiMatKhau(FirebaseUser firebaseUser) {
        strMKMoi = binding.edDMKMatKhauMoi.getText().toString();
        strMkNhapLai = binding.edDMKMatKhauNhapLai.getText().toString();

        if(TextUtils.isEmpty(strMKMoi)){
            binding.edDMKMatKhauMoi.setError("Vui lòng nhập mật khẩu mới");
        } else if (TextUtils.isEmpty(strMkNhapLai)) {
            binding.edDMKMatKhauNhapLai.setError("Vui lòng nhập mật khẩu mới");
        }else if(!strMkNhapLai.matches(strMKMoi)){
            binding.edDMKMatKhauNhapLai.setError("Mật khẩu nhập lại không khớp");
        } else if (strMKMoi.matches(strMKHienTai)) {
            binding.edDMKMatKhauMoi.setError("Mật khẩu mới phải khác mật khẩu hiện tại");
        }else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseUser.updatePassword(strMkNhapLai).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(DoiMatKhauActivity.this, "Mật khẩu đã được thay đổi", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoiMatKhauActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(DoiMatKhauActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}