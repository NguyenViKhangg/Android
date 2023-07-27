package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.DTO.NguoiDungDTO;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Trang chủ");

       firebaseAuth = FirebaseAuth.getInstance();
       FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

       if(firebaseUser == null){
           Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
       }else {
           funcHienThiThongTin(firebaseUser);
       }
    }

    private void funcHienThiThongTin(FirebaseUser firebaseUser) {
        String strUid = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NguoiDung");
        databaseReference.child(strUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDungDTO nguoiDungDTO = snapshot.getValue(NguoiDungDTO.class);
                if(nguoiDungDTO != null){
                    String strHoTen = nguoiDungDTO.HoTen;
                    String strSdt = nguoiDungDTO.SDT;
                    String strEmail = firebaseUser.getEmail();

                    binding.tvHoTen.setText(strHoTen);
                    binding.tvSdt.setText(strSdt);
                    binding.tvEmail.setText(strEmail);
                    binding.tvChaoMung.setText("Xin chào, " + strHoTen + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Lỗi !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menuRefresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        } else if (id == R.id.menuTen) {
            Intent intent = new Intent(MainActivity.this, CapNhatThongTinActivity.class);
            startActivity(intent );
        }
        else if (id == R.id.menuMail) {
            Intent intent = new Intent(MainActivity.this, DoiEmailActivity.class);
            startActivity(intent );
        }
        else if (id == R.id.menuMatKhau) {
//            Intent intent = new Intent(MainActivity.this, DoiMatKhau.class);
//            startActivity(intent);
        }
        else if (id == R.id.menuDangXuat) {
            Toast.makeText(this, "Dang xuat", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);

            // Clear stack
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "Loi", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}