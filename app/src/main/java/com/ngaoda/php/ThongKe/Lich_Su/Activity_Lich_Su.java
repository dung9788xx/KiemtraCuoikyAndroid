package com.ngaoda.php.ThongKe.Lich_Su;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.MoneyType;
import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;
import com.ngaoda.php.ThongKe.Class_HoaDon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Activity_Lich_Su extends AppCompatActivity implements AsyncResponse,Adapter_Lich_Su.MyClickListener {
    EditText edttu, edtden;
    TextView notresult,stt,tenkh,ngay,tt,tvtt;
    ArrayList<Class_HoaDon> list=new ArrayList<>();
    Adapter_Lich_Su adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke_activity__lich__su);
        setTitle(Html.fromHtml("<h3>Lịch sử</h3>"));
        anhxa();
        findViewById(R.id.datepick1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear;
                int mMonth;
                int mDay;
                if (edttu.getText().toString().equals("")) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH)+1;
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    String s[] = edttu.getText().toString().split("/");
                    mYear = Integer.parseInt(s[2]);
                    mMonth = Integer.parseInt(s[1]);
                    mDay = Integer.parseInt(s[0]);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Lich_Su.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edttu.setText(dayOfMonth + "/" + (monthOfYear+1 ) + "/" + year);
                            }
                        }, mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.datepick2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                int mYear;
                int mMonth;
                int mDay;
                if (edtden.getText().toString().equals("")) {
                    mYear = c1.get(Calendar.YEAR);
                    mMonth = c1.get(Calendar.MONTH)+1;
                    mDay = c1.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] s = edtden.getText().toString().split("/");
                    mYear = Integer.parseInt(s[2]);
                    mMonth = Integer.parseInt(s[1]);
                    mDay = Integer.parseInt(s[0]);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Lich_Su.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtden.setText(dayOfMonth + "/" + (monthOfYear+1 ) + "/" + year);

                            }
                        }, mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.timkiem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(!edtden.getText().toString().equals("")&&!edttu.getText().toString().equals("")){
                    String url="http://123.123.123.123/ttcn/index.php?task=gethoadonbyday";
                    Map<String,String > map=new HashMap<>();
                    String []s=edttu.getText().toString().split("/");
                   String tungay=s[2]+"/"+s[1]+"/"+s[0];
                    map.put("tungay",tungay);
                    String []s1=edtden.getText().toString().split("/");
                    String dengay=s1[2]+"/"+s1[1]+"/"+s1[0];
                  map.put("denngay",dengay);
                    TaskConnect t=new TaskConnect( Activity_Lich_Su.this,url );
                    t.setMap( map );
                    t.execute(  );
                    progressBar.setVisibility(View.VISIBLE);
                 notresult.setVisibility(View.INVISIBLE);

                }else Toast.makeText(Activity_Lich_Su.this,"Vui lòng chon ngày",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void anhxa() {
        edttu = findViewById(R.id.edttungay);
        edtden = findViewById(R.id.edtdenngay);
        stt=findViewById(R.id.stt);
        tenkh=findViewById(R.id.tenkh);
        ngay=findViewById(R.id.ngay);
        recyclerView=findViewById(R.id.recy);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new  Adapter_Lich_Su(this,list);
        recyclerView.setAdapter(adapter);
        notresult=findViewById(R.id.notresult);
        progressBar=findViewById(R.id.progress);
        tt=findViewById(R.id.tt);
        tvtt=findViewById(R.id.tvtt);

    }

    @Override
    public void whenfinish(String output) {
        try{
            if(output.contains("gethoadon")){
               list.clear();
                JSONArray jsonArray=new JSONArray(output.substring(output.indexOf("|")+1));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    JSONArray jsonArray1=new JSONArray(object.getString("mon"));
                    ArrayList<ClassMon> listmon=new ArrayList<>();
                    for(int j=0;j<jsonArray1.length();j++){
                        JSONObject object1=jsonArray1.getJSONObject(j);
                        listmon.add(new ClassMon(object1.getString("mamon"),object1.getString("tenmon"),Integer.parseInt(object1.getString("sl")),
                                Integer.parseInt(object1.getString("gia"))));

                    }
                list.add(new Class_HoaDon(listmon,object.getString("mahd"),object.getString("tenban")
                        ,object.getString("tenkh")
                        ,object.getString("khachtra")
                        ,object.getString("ghichu")
                        ,object.getString("tennvdat")
                        ,object.getString("tennvtt")
                        ,object.getString("ngay")
                        ,object.getString("thoigian")));

                }

                adapter.notifyDataSetChanged();
            }
            if(output.contains("error")) Toast.makeText(this,"Có lỗi xảy ra vui lòng thử lại!",Toast.LENGTH_LONG).show();
            if(list.size()>0){
                tinhtongtien();
                stt.setVisibility(View.VISIBLE);
                ngay.setVisibility(View.VISIBLE);
                tenkh.setVisibility(View.VISIBLE);
                notresult.setVisibility(View.INVISIBLE);
            }else {
                tvtt.setVisibility(View.GONE);
                tt.setVisibility(View.GONE);
                stt.setVisibility(View.GONE);
                ngay.setVisibility(View.GONE);
                tenkh.setVisibility(View.GONE);
                notresult.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.INVISIBLE);

        } catch (JSONException e){
            Toast.makeText(this,"LOL:"+e.toString(),Toast.LENGTH_LONG).show();
        }
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
