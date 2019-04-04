package com.ngaoda.php.QLNhanVien.TinhLuong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QLNhanVien.ClassNhanVien;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Activity_ChiTietNgayCong extends AppCompatActivity implements AsyncResponse{
    ListView lv;
    List<ClassNhanVien> list=new ArrayList<>();
    ArrayAdapter<ClassNhanVien> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlnhanvien_tinhluong_activity__chi_tiet_ngay_cong);
        setTitle(Html.fromHtml("<h5>Thông tin ngày công</h5>"));
        anhxa();
        String url = "http://123.123.123.123/ttcn/index.php?task=getchitietngaycong";
        Map<String,String> map=new HashMap<>();
        TaskConnect t = new TaskConnect(this, url);
        map.put("manv",getIntent().getExtras().getString("manv"));
        map.put("curdate",getIntent().getExtras().getString("curdate"));
        t.setMap(map);
        t.execute();
    }

    private void anhxa() {
        lv=findViewById(R.id.lv);
    }

    @Override
    public void whenfinish(String output) {
        if(output.contains("getchitiet")) {
            list.clear();
            try {

                JSONArray jsonArray = new JSONArray(output.substring(output.indexOf("|") + 1));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    list.add(new ClassNhanVien(null, null, object.getString("ngay"), object.getString("denlam")));

                }
                arrayAdapter = new ArrayAdapter<ClassNhanVien>(this, R.layout.qlnhanvien_tinhluong_item_thongtin, list) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.qlnhanvien_tinhluong_item_thongtin, null);
                        TextView stt = (TextView) view.findViewById(R.id.stt);
                        TextView ngay = (TextView) view.findViewById(R.id.ngay);
                        CheckBox checkBox = view.findViewById(R.id.cb);
                        ClassNhanVien nhanVien = list.get(position);
                        stt.setText(position + 1 + ".");
                        ngay.setText(nhanVien.ngaysinh);
                        if (nhanVien.denlam.equals("1")) {
                            checkBox.setChecked(true);
                        } else checkBox.setChecked(false);
                        return view;
                    }
                };
                lv.setAdapter(arrayAdapter);
            } catch (JSONException e) {

            }
        }

    }
}
