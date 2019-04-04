package com.ngaoda.php.QLNhanVien.TinhLuong;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.MoneyType;
import com.ngaoda.php.QLNhanVien.ClassNhanVien;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Activity_TinhLuong extends Activity implements AsyncResponse {
    TextView tvthang;
    EditText edtsongay;
    Button bttinh;
    List<ClassNhanVien> list=new ArrayList<>();
    ListView lv;
    ArrayAdapter arrayAdapter;
    String curdate="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlnhanvien_tinhluong_activity_tinh_luong);
        anhxa();
        findViewById(R.id.content).setVisibility(View.GONE);
        loadngay();

        findViewById(R.id.bttinh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinhluong();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent t=new Intent(Activity_TinhLuong.this,Activity_ChiTietNgayCong.class);
                t.putExtra("manv",list.get(position).getManv());
                t.putExtra("curdate",curdate);
                startActivity(t);
            }
        });
        findViewById(R.id.tvthang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               createDialogWithoutDateField().show();

            }
        });
    }
    private DatePickerDialog createDialogWithoutDateField() {
        final Calendar c = Calendar.getInstance();
        int mYear;
        int mMonth;
        int mDay;
        mYear = c.get(Calendar.YEAR);
        mMonth = Integer.parseInt(tvthang.getText().toString())-1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                findViewById(R.id.content).setVisibility(View.GONE);
                findViewById(R.id.progress).setVisibility(View.VISIBLE);
                curdate=year+"/"+(month+1)+"/"+dayOfMonth;
                loadngay1();


            }
        }, mYear, mMonth, mDay);

        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
        }

        return dpd;
    }

    private void loadngay() {
        String url = "http://123.123.123.123/ttcn/index.php?task=getdate";
        TaskConnect t = new TaskConnect(this, url);
        t.execute();
    }
    private void loadngay1() {
        String url = "http://123.123.123.123/ttcn/index.php?task=getsongaycuathang";
        TaskConnect t = new TaskConnect(this, url);
        Map<String,String> map=new HashMap<>();
        map.put("curdate",curdate);
        t.setMap(map);
        t.execute();
    }

    private void anhxa() {
        tvthang=findViewById(R.id.tvthang);
        edtsongay=findViewById(R.id.editsongay);
        bttinh=findViewById(R.id.bttinh);
        lv=findViewById(R.id.lv);


    }

    @Override
    public void whenfinish(String output) {
        if(output.contains("getdate")){
            curdate=output.substring(output.indexOf("|")+1);
            loadngay1();
        }
        if(output.contains("getsongay")){

            tvthang.setText(output.substring(output.indexOf("|")+1,output.lastIndexOf("|")));
            edtsongay.setText(output.substring(output.lastIndexOf("|")+1));
            findViewById(R.id.content).setVisibility(View.VISIBLE);
            findViewById(R.id.progress).setVisibility(View.GONE);
            tinhluong();
        }
        if(output.contains("tinhluong")){
            try{
                list.clear();
                JSONArray jsonArray=new JSONArray(output.substring(output.indexOf("|")+1));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    list.add(new ClassNhanVien(object.getString("manv"),object.getString("hoten"),
                            object.getString("ngaysinh"),
                            object.getString("denlam")));
                }
                arrayAdapter=new ArrayAdapter<ClassNhanVien>(this,R.layout.qlnhanvien_tinhluong_item,list){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view=inflater.inflate(R.layout.qlnhanvien_tinhluong_item,null);
                        TextView hoten = (TextView) view.findViewById(R.id.hoten);
                        TextView stt = (TextView) view.findViewById(R.id.stt);
                        TextView ngaysinh = (TextView) view.findViewById(R.id.ngaysinh);
                        TextView tien=view.findViewById(R.id.tien);
                        ClassNhanVien nhanVien=list.get(position);
                        stt.setText(position+1+".");
                        hoten.setText(nhanVien.getHoten());
                        ngaysinh.setText(nhanVien.getNgaysinh());
                        double db=Double.valueOf(nhanVien.denlam);
                        long l=(long)db;
                        tien.setText(MoneyType.toMoney(l) +" VND");
                        return view;
                    }
                };
                lv.setAdapter(arrayAdapter);


            }catch (JSONException e){
                Toast.makeText(this,output,Toast.LENGTH_LONG).show();
            }
            if(list.size()>0){
                findViewById(R.id.notresult).setVisibility(View.GONE);
            }else{
                findViewById(R.id.notresult).setVisibility(View.VISIBLE);
            }
        }

    }

    private void tinhluong() {
        String url = "http://123.123.123.123/ttcn/index.php?task=tinhluong";
        Map<String,String> map=new HashMap<>();
        TaskConnect t = new TaskConnect(this, url);
        map.put("songay",edtsongay.getText().toString());
        map.put("curdate",curdate);
        t.setMap(map);
        t.execute();
    }
}
