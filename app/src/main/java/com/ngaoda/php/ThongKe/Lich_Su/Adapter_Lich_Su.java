package com.ngaoda.php.ThongKe.Lich_Su;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngaoda.php.R;
import com.ngaoda.php.ThongKe.Class_HoaDon;

import java.util.ArrayList;

public class Adapter_Lich_Su extends RecyclerView.Adapter<Adapter_Lich_Su.ViewHolder> {
    ArrayList<Class_HoaDon> list;
    MyClickListener clickListener;
   public Adapter_Lich_Su(MyClickListener clickListener, ArrayList<Class_HoaDon> list){
    this.list=list;
    this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View v=layoutInflater.inflate(R.layout.thongke_lich_su_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Class_HoaDon class_hoaDon=list.get(i);
        viewHolder.stt.setText((i+1)+".");
        viewHolder.tenkh.setText(class_hoaDon.tenkh);
        String s[]=class_hoaDon.ngay.split("-");
        viewHolder.ngaygio.setText(s[2]+"/"+s[1]+"/"+s[0]);

    }

    @Override
    public int getItemCount() {
       return list.size();

    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
       TextView stt,tenkh,ngaygio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stt=itemView.findViewById(R.id.stt);
            tenkh=itemView.findViewById(R.id.tenkh);
            ngaygio=itemView.findViewById(R.id.ngaygio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.OnItemClicked(getPosition());
                }
            });

        }
    }
    public  interface MyClickListener{
        void OnItemClicked(int position);
    }
}
