package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.DTO.NguoiDungDTO;
import com.example.myapplication.databinding.LayoutCapNhatThongTinBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CapNhatThongTinActivity extends AppCompatActivity {
    LayoutCapNhatThongTinBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutCapNhatThongTinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressBar = binding.progressBar;
        getSupportActionBar().setTitle("Cập nhật thông tin");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        funcHienThiThongTin(firebaseUser);

        binding.btnCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funcCapNhatThongTin(firebaseUser);
            }
        });
    }

    private void funcCapNhatThongTin(FirebaseUser firebaseUser) {
        String strHoTen = binding.edCapNhatHoTen.getText().toString();
        String strSdt = binding.edCapNhatSdt.getText().toString();

        if(TextUtils.isEmpty(strHoTen)){
            binding.edCapNhatHoTen.setError("Vui lòng nhập họ và tên");
        }else if(TextUtils.isEmpty(strSdt)) {
            binding.edCapNhatSdt.setError("Vui lòng nhập số điện thoại");
        } else if (strSdt.length() != 10 ) {
            binding.edCapNhatSdt.setError("Vui lòng nhập lại số điện thoại");
        }else {
            NguoiDungDTO nguoiDungDTO = new NguoiDungDTO(strHoTen, strSdt);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NguoiDung");
            String strUid = firebaseUser.getUid();
            progressBar.setVisibility(View.VISIBLE);

            databaseReference.child(strUid).setValue(nguoiDungDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CapNhatThongTinActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CapNhatThongTinActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        try{
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(CapNhatThongTinActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void funcHienThiThongTin(FirebaseUser firebaseUser) {
        String strUid = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NguoiDung");
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(strUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDungDTO nguoiDungDTO = snapshot.getValue(NguoiDungDTO.class);
                if(nguoiDungDTO != null){
                    binding.edCapNhatHoTen.setText(nguoiDungDTO.HoTen);
                    binding.edCapNhatSdt.setText(nguoiDungDTO.SDT);
                }else {
                    Toast.makeText(CapNhatThongTinActivity.this, "Lỗi !", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CapNhatThongTinActivity.this, "Lỗi !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}