package com.ngaoda.php.ThongKe.Nhan_Vien;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QLNhanVien.ClassNhanVien;
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

public class Activity_Theo_Nhan_Vien extends AppCompatActivity implements AsyncResponse {
    ArrayList<ClassNhanVien> listnv = new ArrayList<>();
    Spinner spinner;
    RadioButton nvdat, nvtt;
    EditText edttu, edtden;
    ProgressBar progressBar;
    TextView notresult;
    ArrayList<Class_HoaDon> listhoadon = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke_activity__theo__nhan__vien);
        setTitle(Html.fromHtml("<h4>Thống kê theo nhân viên</h4>"));
        anhxa();
        loaddsnhanvien();
        findViewById(R.id.datepick1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear;
                int mMonth;
                int mDay;
                if (edttu.getText().toString().equals("")) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH) + 1;
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    String s[] = edttu.getText().toString().split("/");
                    mYear = Integer.parseInt(s[2]);
                    mMonth = Integer.parseInt(s[1]);
                    mDay = Integer.parseInt(s[0]);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Theo_Nhan_Vien.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edttu.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth - 1, mDay);
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
                    mMonth = c1.get(Calendar.MONTH) + 1;
                    mDay = c1.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] s = edtden.getText().toString().split("/");
                    mYear = Integer.parseInt(s[2]);
                    mMonth = Integer.parseInt(s[1]);
                    mDay = Integer.parseInt(s[0]);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Theo_Nhan_Vien.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtden.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth - 1, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.timkiem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtden.getText().toString().equals("") && !edttu.getText().toString().equals("")) {
                 if(nvdat.isChecked()||nvtt.isChecked()){
                     String url = "http://123.123.123.123/ttcn/index.php?task=thongketheonhanvien";
                     Map<String, String> map = new HashMap<>();
                     String[] s = edttu.getText().toString().split("/");
                     String tungay = s[2] + "/" + s[1] + "/" + s[0];
                     map.put("tungay", tungay);
                     String[] s1 = edtden.getText().toString().split("/");
                     String dengay = s1[2] + "/" + s1[1] + "/" + s1[0];
                     map.put("denngay", dengay);
                     map.put("manv", listnv.get(spinner.getSelectedItemPosition()).getManv());
                     if (nvdat.isChecked()) {
                         map.put("type", "1");
                     }else map.put("type", "2");
                     TaskConnect t = new TaskConnect(Activity_Theo_Nhan_Vien.this, url);
                     t.setMap(map);
                     t.execute();
                     progressBar.setVisibility(View.VISIBLE);
                     notresult.setVisibility(View.INVISIBLE);
                 }else  Toast.makeText(Activity_Theo_Nhan_Vien.this, "Vui lòng chọn kiểu thống kê!", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(Activity_Theo_Nhan_Vien.this, "Vui lòng chon ngày!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void anhxa() {
        spinner = findViewById(R.id.spiner);
        nvdat = findViewById(R.id.nvdat);
        nvtt = findViewById(R.id.nvtt);
        edttu = findViewById(R.id.edttungay);
        edtden = findViewById(R.id.edtdenngay);
        progressBar = findViewById(R.id.progress);
        notresult = findViewById(R.id.notresult);
    }

    private void loaddsnhanvien() {
        String url = "http://123.123.123.123/ttcn/index.php?task=getlistnhanvien";
        TaskConnect t = new TaskConnect(this, url);
        t.execute();
    }

    @Override
    public void whenfinish(String output) {
        try {
            if (output.contains("getlistnhanvien")) {

                JSONArray jsonArray = new JSONArray(output.substring(output.indexOf("|") + 1));
                listnv.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    listnv.add(new ClassNhanVien(object.getString("manv"), object.getString("hoten")));
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_mon_an, listnv) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.thongke_theo_nhanvien_item_nhanvien, null);
                        TextView manv = view.findViewById(R.id.manv);
                        TextView hoten = view.findViewById(R.id.hoten);
                        ClassNhanVien nhanVien = listnv.get(position);
                        hoten.setText(nhanVien.getHoten());
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.thongke_theo_nhanvien_item_nhanvien1, null);
                        TextView manv = view.findViewById(R.id.manv);
                        TextView hoten = view.findViewById(R.id.hoten);
                        ClassNhanVien nhanVien = listnv.get(position);
                        manv.setText(nhanVien.getManv());
                        hoten.setText(nhanVien.getHoten());


                        return view;
                    }
                };
                spinner.setAdapter(arrayAdapter);
            }
            if (output.contains("thongketheonhanvien")) {
                listhoadon.clear();
                JSONArray jsonArray = new JSONArray(output.substring(output.indexOf("|") + 1));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = new JSONArray(object.getString("mon"));
                    ArrayList<ClassMon> listmon = new ArrayList<>();
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject object1 = jsonArray1.getJSONObject(j);
                        listmon.add(new ClassMon(object1.getString("mamon"), object1.getString("tenmon"), Integer.parseInt(object1.getString("sl")),
                                Integer.parseInt(object1.getString("gia"))));

                    }
                    listhoadon.add(new Class_HoaDon(listmon, object.getString("mahd"), object.getString("tenban")
                            , object.getString("tenkh")
                            , object.getString("khachtra")
                            , object.getString("ghichu")
                            , object.getString("tennvdat")
                            , object.getString("tennvtt")
                            , object.getString("ngay")
                            , object.getString("thoigian")));

                }
                if(listhoadon.size()>0){
                    progressBar.setVisibility(View.GONE);
                    Intent t=new Intent(this,Activity_Hien_Thi_Info.class);
                    t.putExtra("list",listhoadon);
                    startActivity(t);

                }else{
                    notresult.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }


            }
        } catch (JSONException e) {

        }
    }

    public void onclick(View view) {
        int id = view.getId();
        if (id == R.id.nvdat)
            nvtt.setChecked(false);
        else nvdat.setChecked(false);
    }
}
