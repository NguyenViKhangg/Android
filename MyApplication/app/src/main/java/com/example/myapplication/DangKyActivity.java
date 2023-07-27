package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.DTO.NguoiDungDTO;
import com.example.myapplication.databinding.LayoutDangKyBinding;
import com.example.myapplication.databinding.LayoutDangNhapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DangKyActivity extends AppCompatActivity {
    private static final String TAG = "DangKiActivity";
    LayoutDangKyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutDangKyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = binding.edDKEmail.getText().toString();
                String strMatKhau = binding.edDKMatKhau.getText().toString();
                String strHoTen = binding.edDKHoTen.getText().toString();
                String strSdt = binding.edDKSDT.getText().toString();

                if (TextUtils.isEmpty(strEmail)){
                    binding.edDKEmail.setError("Vui lòng nhập email");
                } else if (TextUtils.isEmpty(strMatKhau)) {
                    binding.edDKMatKhau.setError("Vui lòng nhập mật khẩu");
                } else if (TextUtils.isEmpty(strHoTen)) {
                    binding.edDKHoTen.setError("Vui lòng nhập họ và tên");
                } else if (TextUtils.isEmpty(strSdt)) {
                    binding.edDKSDT.setError("Vui lòng nhập số điện thoại");
                } else {
                    funcDangKi(strEmail, strMatKhau, strHoTen, strSdt);
                }
            }
        });

        binding.tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                startActivity(intent);
            }
        });

    }

    private void funcDangKi(String email, String matKhau, String hoTen, String sdt) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    NguoiDungDTO nguoiDungDTO = new NguoiDungDTO(hoTen, sdt);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NguoiDung");
                    databaseReference.child(firebaseUser.getUid()).setValue(nguoiDungDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
//                                firebaseUser.sendEmailVerification();
                                Toast.makeText(DangKyActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e){
                        binding.edDKEmail.setError("Email da duoc su dung");
                    }catch (FirebaseAuthWeakPasswordException e) {
                        binding.edDKMatKhau.setError("Mat khau phai lon hon 6 ky tu");
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        binding.edDKEmail.setError("Email khong dung dinh dang");
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(DangKyActivity.this, "Dang ki that bai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}