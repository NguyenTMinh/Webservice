package com.example.webservicetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.SinhVienHolder> {
    private List<SinhVien> list;
    private Context mContext;
    private ICallBack callBack;

    public interface ICallBack{
        public void onDel(int position,int id);
        public void onEdit(int position,int id);
    }

    public SinhVienAdapter(List<SinhVien> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SinhVienHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.line_sinhvien,parent,false);
        return new SinhVienHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SinhVienHolder holder, int position) {
        SinhVien sinhVien = list.get(position);
        holder.tvName.setText(sinhVien.getName());
        holder.tvDate.setText(sinhVien.getDateString());
        holder.tvAdd.setText(sinhVien.getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setCallBack(ICallBack callBack) {
        this.callBack = callBack;
    }

    public void release(){
        mContext = null;
    }

    class SinhVienHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvDate;
        TextView tvAdd;
        ImageView ivDel;
        ImageView ivEdt;

        public SinhVienHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_namsinh);
            tvAdd = itemView.findViewById(R.id.tv_diachi);
            ivDel = itemView.findViewById(R.id.iv_delete);
            ivEdt = itemView.findViewById(R.id.iv_edit);
            ivDel.setOnClickListener(v -> callBack.onDel(getAdapterPosition(),list.get(getAdapterPosition()).getId()));
            ivEdt.setOnClickListener(v -> callBack.onEdit(getAdapterPosition(),list.get(getAdapterPosition()).getId()));
        }
    }
}
