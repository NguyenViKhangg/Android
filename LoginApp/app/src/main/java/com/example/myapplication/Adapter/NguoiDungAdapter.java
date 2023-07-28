package com.example.myapplication.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DTO.NguoiDungDTO;
import com.example.myapplication.R;

import java.util.ArrayList;

public class NguoiDungAdapter extends RecyclerView.Adapter<NguoiDungAdapter.MyViewHolder> {

    Context context;
    ArrayList<NguoiDungDTO> nguoiDungList;

    public NguoiDungAdapter(Context context, ArrayList<NguoiDungDTO> nguoiDungList) {
        this.context = context;
        this.nguoiDungList = nguoiDungList;
    }

    @NonNull
    @Override
    public NguoiDungAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rl_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NguoiDungAdapter.MyViewHolder holder, int position) {

        NguoiDungDTO nguoiDungDTO = nguoiDungList.get(position);
        holder.tvHoTen.setText(nguoiDungDTO.getHoTen());
        holder.tvSdt.setText(nguoiDungDTO.getSdt());

    }

    @Override
    public int getItemCount() {
        return nguoiDungList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvHoTen, tvSdt, tvEMail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHoTen = itemView.findViewById(R.id.tvNDHoTen);
            tvSdt = itemView.findViewById(R.id.tvNDSdt);
            tvEMail = itemView.findViewById(R.id.tvNDEmail);

        }
    }
}
