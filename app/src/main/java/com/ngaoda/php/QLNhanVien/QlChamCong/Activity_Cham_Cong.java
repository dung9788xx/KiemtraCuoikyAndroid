package com.ngaoda.php.QLNhanVien.QlChamCong;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Activity_Cham_Cong extends AppCompatActivity implements AsyncResponse {
    TextView tvngay;
    String ngay;
    List<ClassNhanVien> listnv=new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cham__cong);
        setTitle(Html.fromHtml("<h5>Chấm công</h5>"));
        anhxa();
        findViewById(R.id.content).setVisibility(View.GONE);
        findViewById(R.id.huy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Activity_Cham_Cong.this)
                        .setMessage("Hủy chấm công ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Activity_Cham_Cong.super.onBackPressed();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
        loaddate();
        checkneudachamcong();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox=view.findViewById(R.id.check);
                checkBox.setChecked(!checkBox.isChecked());
                    if(listnv.get(position).denlam.equals("1")){
                        listnv.get(position).denlam="0";
                    }else  listnv.get(position).denlam="1";


            }
        });
        findViewById(R.id.luu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    luu();
            }
        });
    }

    private void luu() {
        JSONArray jsArray = new JSONArray();
        for (ClassNhanVien f : listnv) {
            JSONObject a = new JSONObject();
            try {
                a.put("manv", f.manv);
                a.put("denlam", f.denlam);
                jsArray.put(a);
            } catch (JSONException e) {

            }

        }
        String url = "http://123.123.123.123/ttcn/index.php?task=chamcong";
        TaskConnect t = new TaskConnect(this, url);
        Map<String, String> map = new HashMap<>();
        map.put("json", jsArray.toString());
        t.setMap(map);
        t.execute();
    }

    private void checkneudachamcong() {
        String url = "http://123.123.123.123/ttcn/index.php?task=checkneudachamcong";
        TaskConnect t = new TaskConnect(this, url);
        t.execute();
    }

    private void loadnhanvien() {
        String url = "http://123.123.123.123/ttcn/index.php?task=getlistnhanvien";
        TaskConnect t = new TaskConnect(this, url);
        t.execute();
    }

    private void anhxa() {
        tvngay=findViewById(R.id.ngay);
        lv=findViewById(R.id.lv);
    }

    private void loaddate() {

            String url = "http://123.123.123.123/ttcn/index.php?task=getdate";
            TaskConnect t = new TaskConnect(this, url);
            t.execute();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Hủy chấm công ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                       Activity_Cham_Cong.super.onBackPressed();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    @Override
    public void whenfinish(String output) {
            if(output.contains("getdate")){
                String sngay[]=output.substring(output.indexOf("|")+1).split("-");
                ngay=sngay[2]+"/"+sngay[1]+"/"+sngay[0];
                tvngay.setText(ngay);
            }
            if(output.contains("dacham")){
                try {
                    JSONArray jsonArray = new JSONArray(output.substring(output.indexOf("|") + 1));
                    listnv.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        listnv.add(new ClassNhanVien(object.getString("manv"), object.getString("hoten"), object.getString("ngaysinh"),object.getString("denlam")));
                    }
                    arrayAdapter = new ArrayAdapter(this, R.layout.qlnhanvien_qlchamcong_itemnhanvien, listnv) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            View view = layoutInflater.inflate(R.layout.qlnhanvien_qlchamcong_itemnhanvien, null);
                            TextView hoten = (TextView) view.findViewById(R.id.hoten);
                            TextView stt = (TextView) view.findViewById(R.id.stt);
                            TextView ngaysinh = (TextView) view.findViewById(R.id.ngaysinh);
                            CheckBox check= view.findViewById(R.id.check);
                            ClassNhanVien n = listnv.get(position);
                            stt.setText(position+1+"");
                            hoten.setText(n.getHoten());
                            ngaysinh.setText(n.getNgaysinh());
                            if(n.denlam.equals("1")){
                                check.setChecked(true);
                            }else check.setChecked(false);
                            return view;
                        }
                    };
                    lv.setAdapter(arrayAdapter);
                    findViewById(R.id.content).setVisibility(View.VISIBLE);
                    findViewById(R.id.progress).setVisibility(View.GONE);

                } catch (JSONException e) {

                }
            }
            if(output.contains("chuacham")){
                loadnhanvien();
            }
            if(output.contains("getlistnhanvien")){
                try {
                    JSONArray jsonArray = new JSONArray(output.substring(output.indexOf("|") + 1));
                    listnv.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        listnv.add(new ClassNhanVien(object.getString("manv"), object.getString("hoten"), object.getString("ngaysinh"),"0"));
                    }
                    arrayAdapter = new ArrayAdapter(this, R.layout.qlnhanvien_qlchamcong_itemnhanvien, listnv) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            View view = layoutInflater.inflate(R.layout.qlnhanvien_qlchamcong_itemnhanvien, null);
                            TextView hoten = (TextView) view.findViewById(R.id.hoten);
                            TextView stt = (TextView) view.findViewById(R.id.stt);
                            TextView ngaysinh = (TextView) view.findViewById(R.id.ngaysinh);
                            ClassNhanVien n = listnv.get(position);
                            stt.setText(position+1+"");
                           hoten.setText(n.getHoten());
                           ngaysinh.setText(n.getNgaysinh());
                            return view;
                        }
                    };
                    lv.setAdapter(arrayAdapter);
                    findViewById(R.id.content).setVisibility(View.VISIBLE);
                    findViewById(R.id.progress).setVisibility(View.GONE);

                } catch (JSONException e) {

                }
            }
            if(output.contains("dachamthanhcong")){
                Toast.makeText(this,"Đã lưu chấm công",Toast.LENGTH_LONG).show();
                finish();
            }
            if(output.contains("error")){
                Toast.makeText(this,"Có lỗi xảy ra vui lòng thử lại!"+output,Toast.LENGTH_LONG).show();
            }
    }
}
