package com.example.myapplication.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DTO.NguoiDungDTO;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class NguoiDungAdapter extends RecyclerView.Adapter<NguoiDungAdapter.MyViewHolder> {

    Context context;
    ArrayList<NguoiDungDTO> nguoiDungList;
    FirebaseAuth firebaseAuth;
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
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.llEx.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(holder.cvItem, new AutoTransition());
                    holder.llEx.setVisibility(View.VISIBLE);
                    holder.ivItem.setImageResource(R.drawable.circle_up);

                }else {
                    TransitionManager.beginDelayedTransition(holder.cvItem, new AutoTransition());
                    holder.llEx.setVisibility(View.GONE);
                    holder.ivItem.setImageResource(R.drawable.circle_down_24);
                }
            }
        });

        holder.btnEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.tvEx.setText(position);
                Toast.makeText(context, holder.tvEx.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return nguoiDungList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvHoTen, tvSdt, tvEMail, tvEx;
        CardView cvItem;
        LinearLayout llEx;
        ImageView ivItem;
        Button btnEx;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHoTen = itemView.findViewById(R.id.tvNDHoTen);
            tvSdt = itemView.findViewById(R.id.tvNDSdt);
            tvEMail = itemView.findViewById(R.id.tvNDEmail);
            llEx = itemView.findViewById(R.id.llEx);
            cvItem = itemView.findViewById(R.id.cvItem);
            ivItem = itemView.findViewById(R.id.ivItem);

            btnEx = itemView.findViewById(R.id.btnEx);
            tvEx = itemView.findViewById(R.id.tvEx);
        }
    }
}
