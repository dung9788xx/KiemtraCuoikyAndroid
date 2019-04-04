package com.ngaoda.php.QlDat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngaoda.php.R;

import java.util.ArrayList;

public class TableStateAdapter extends  RecyclerView.Adapter<TableStateAdapter.ViewHolder>{
    ArrayList<ClassTable> list;
    MyClickListener myClickListener;
    public TableStateAdapter(MyClickListener myClickListener, ArrayList<ClassTable> list) {
        this.list = list;
        this.myClickListener=myClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from( parent.getContext() );
        View view=layoutInflater.inflate( R.layout.table_state_item,parent,false );

        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tenban.setText( list.get( position ).tenban );
        if(list.get( position ).state.equals( "1" )){
            holder.img.setImageResource( R.drawable.table_busy );
        }else holder.img.setImageResource( R.drawable.ic_table1 );

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView tenban;
        ImageView img;
        public ViewHolder(View itemView) {
            super( itemView );
            tenban=(TextView) itemView.findViewById( R.id.tenban );
            img=(ImageView) itemView.findViewById( R.id.img );
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.OnItemClicked( getPosition() );
                }
            } );

        }
    }
    public  interface MyClickListener{
        void OnItemClicked(int position);
    }
}
