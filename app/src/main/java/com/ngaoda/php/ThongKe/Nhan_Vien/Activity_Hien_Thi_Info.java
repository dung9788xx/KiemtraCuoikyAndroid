package com.ngaoda.php.ThongKe.Nhan_Vien;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ngaoda.php.MoneyType;
import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;
import com.ngaoda.php.ThongKe.Class_HoaDon;
import com.ngaoda.php.ThongKe.Lich_Su.Adapter_Lich_Su;
import com.ngaoda.php.ThongKe.Lich_Su.Fragment_thong_tin;

import java.util.ArrayList;

public class Activity_Hien_Thi_Info extends AppCompatActivity implements Adapter_Lich_Su.MyClickListener {
ArrayList<Class_HoaDon> list =new ArrayList<>();
    Adapter_Lich_Su adapter;
    RecyclerView recyclerView;
    TextView tt,tvtt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke_nhanvien_activity__hien__thi__info);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            list=(ArrayList<Class_HoaDon>)bundle.getSerializable("list");

        }
        anhxa();
        tinhtongtien();
    }

    private void anhxa() {
        recyclerView=findViewById(R.id.recy);
        tt=findViewById(R.id.tt);
        tvtt=findViewById(R.id.tvtt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new  Adapter_Lich_Su(this,list);
        recyclerView.setAdapter(adapter);
    }
    private void tinhtongtien() {
        long tongtien=0;
        for(int i=0;i<list.size();i++){
            ArrayList<ClassMon> classMon=list.get(i).listmon;
            for(int j=0;j<classMon.size();j++){
                tongtien=tongtien+(classMon.get(j).sl*classMon.get(j).gia);
            }
        }
        tvtt.setVisibility(View.VISIBLE);
        tt.setText(MoneyType.toMoney(tongtien)+ "VND");
        tt.setVisibility(View.VISIBLE);

    }

    @Override
    public void OnItemClicked(int position) {
        FragmentManager fragmentManager =getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment_thong_tin fr=new Fragment_thong_tin();
        Bundle bundle=new Bundle();
        bundle.putSerializable("tt",list.get(position));
        fr.setArguments(bundle);
        fragmentTransaction.add(R.id.frame,fr);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager =getFragmentManager();
        Fragment fragmentA = fragmentManager.findFragmentById(R.id.frame);
        if (fragmentA == null) {
            super.onBackPressed();
        }
        else{
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentA);
            fragmentTransaction.commit();

        }
    }
}
