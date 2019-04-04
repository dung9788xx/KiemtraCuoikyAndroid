package com.ngaoda.php.ThongKe.Theo_Mon;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;

import java.util.ArrayList;

public class Adapter_Theo_Mon extends RecyclerView.Adapter<Adapter_Theo_Mon.ViewHolder>{
    MyClickListenner clickListenner;
    ArrayList<ClassMon> mons;
    public Adapter_Theo_Mon(Adapter_Theo_Mon.MyClickListenner clickListenner, ArrayList<ClassMon> mons){
        this.clickListenner=clickListenner;
        this.mons=mons;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View v=layoutInflater.inflate(R.layout.thongke_theo_mon_item,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ClassMon mon=mons.get(i);
        viewHolder.mamon.setText(mon.getMamon()+"");
        viewHolder.tenmon.setText(mon.tenmon);
        viewHolder.sl.setText(mon.sl+"");
    }

    @Override
    public int getItemCount() {
        return mons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tenmon,mamon,sl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenmon=itemView.findViewById(R.id.tenmon);
            mamon=itemView.findViewById(R.id.mamon);
            sl=itemView.findViewById(R.id.sl);

        }
    }
    public interface MyClickListenner{
        public void OnItemClicked(int position);
    }

}
