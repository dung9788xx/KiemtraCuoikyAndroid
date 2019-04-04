package com.ngaoda.php.ThongKe.Lich_Su;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ngaoda.php.MoneyType;
import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;
import com.ngaoda.php.ThongKe.Class_HoaDon;

import java.util.ArrayList;

public class Fragment_thong_tin extends Fragment {

    public Fragment_thong_tin(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_thong_tin, container,false );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=this.getArguments();
        Class_HoaDon class_hoaDon=(Class_HoaDon)bundle.getSerializable("tt");
        TextView mahd,tenkh,khachtra,tenban,ghichu,tennvdat,tennvtt,thoigian,tongtien;
        final ArrayList<ClassMon> classMons=class_hoaDon.listmon;
        ListView lv;
        ArrayAdapter <ClassMon> arrayAdapter=new ArrayAdapter<ClassMon>(getActivity(),R.layout.booking_item,classMons){

            @Override
            public View getView(int position,View convertView,  ViewGroup parent) {
                LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View view=inflater.inflate( R.layout.booking_item,null );
                TextView tenmon=(TextView) view.findViewById( R.id.tenmon );
                TextView sl=(TextView) view.findViewById( R.id.sl );
                TextView tongtien=(TextView) view.findViewById( R.id.tongtien );
                ClassMon f=classMons.get( position );
                tenmon.setText( f.getTenmon() );
                sl.setText( f.getSl()+"" );
                tongtien.setText( (MoneyType.toMoney(f.getSl()*f.getGia())) );
                return view;
            }
        };
        mahd=view.findViewById(R.id.mahd);
        tenkh=view.findViewById(R.id.tenkh);
        khachtra=view.findViewById(R.id.khachtra);
        tenban=view.findViewById(R.id.tenban);
        ghichu=view.findViewById(R.id.ghichu);
        tennvdat=view.findViewById(R.id.tennvdat);
        tennvtt=view.findViewById(R.id.tennvtt);
        thoigian=view.findViewById(R.id.thoigian);
        lv=view.findViewById(R.id.lv);
        tongtien=view.findViewById(R.id.tongtien);
        int tong=0;
        for(ClassMon f:classMons){
            tong=tong+(f.getSl()*f.gia);
        }
        tongtien.setText(MoneyType.toMoney(tong));
        lv.setAdapter(arrayAdapter);
        mahd.setText(class_hoaDon.mahd);
        tenkh.setText(class_hoaDon.tenkh);
        khachtra.setText(MoneyType.toMoney(Integer.parseInt(class_hoaDon.khachtra)));
        tenban.setText(class_hoaDon.tenban);
        ghichu.setText(class_hoaDon.ghichu);
        tennvdat.setText(class_hoaDon.tennvdat);
        tennvtt.setText(class_hoaDon.tennvtt);
        thoigian.setText(class_hoaDon.ngay+" "+class_hoaDon.thoigian);


    }
}
