package com.ngaoda.php.QLNhanVien.DanhSach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AdminActivity;
import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QLNhanVien.Activity_QLNhanVien;
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

public class Activity_List_NhanVien extends AppCompatActivity implements AsyncResponse {
    ListView lv;
    List<ClassNhanVien> listnv = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlnv_activity_nhan_vien);
        setTitle(Html.fromHtml("<h3>Danh sách nhân viên</h3>"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_List_NhanVien.this, Activity_Add_NhanVien.class));
            }
        });
        anhxa();
        loaddata();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (AdminActivity.username.equals("admin")) {
                    if(!listnv.get(position).username.equals("admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_List_NhanVien.this);
                    builder.setMessage("Xác nhận xóa nhân viên?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String url = "http://123.123.123.123/ttcn/index.php?task=xoanhanvien";
                            Map<String, String> map = new HashMap<>();
                            map.put("username", listnv.get(position).getUsername());
                            TaskConnect t = new TaskConnect(Activity_List_NhanVien.this, url);
                            t.setMap(map);
                            t.execute();
                            loaddata();
                            dialogInterface.dismiss();

                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                        Toast.makeText(Activity_List_NhanVien.this, "Không được phép xóa tài khoản này!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(Activity_List_NhanVien.this, "Bạn không có quyền xóa !", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent t = new Intent(Activity_List_NhanVien.this, Activity_Edit_NhanVien.class);
                t.putExtra("nv", listnv.get(position));
                startActivity(t);
            }
        });

    }

    public void anhxa() {
        lv = (ListView) findViewById(R.id.lv);
        error = findViewById(R.id.connecterror);

    }

    @Override
    public void whenfinish(String output) {
        if (output.contains("getlistnhanvien")) {
            try {
                JSONArray jsonArray = new JSONArray(output.substring(output.indexOf("|") + 1));
                listnv.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    listnv.add(new ClassNhanVien(object.getString("manv"), object.getString("hoten"), object.getString("ngaysinh")
                            , object.getString("diachi"), object.getString("sdt"), object.getString("username")
                            , object.getString("password"), object.getString("level"), object.getString("luong")));
                }
                arrayAdapter = new ArrayAdapter(this, R.layout.item_nhanvien, listnv) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.item_nhanvien, null);
                        TextView hoten = (TextView) view.findViewById(R.id.hoten);
                        TextView username = (TextView) view.findViewById(R.id.username);
                        TextView password = (TextView) view.findViewById(R.id.password);
                        TextView sdt = (TextView) view.findViewById(R.id.sdt);
                        ClassNhanVien n = listnv.get(position);
                        hoten.setText(n.getHoten());
                        sdt.setText(n.getSdt());
                        username.setText("User: " + n.getUsername());
                        password.setText("Password:  " + n.getPassword());
                        return view;
                    }
                };
                lv.setAdapter(arrayAdapter);
                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.content).setVisibility(View.VISIBLE);
            } catch (JSONException e) {

            }
        }
        if (output.contains("using"))
            Toast.makeText(Activity_List_NhanVien.this, "Tài khoản đang được sử dụng!", Toast.LENGTH_LONG).show();
        if (output.contains("deleted"))
            Toast.makeText(Activity_List_NhanVien.this, "Xóa thành công", Toast.LENGTH_LONG).show();
        if (output.contains("error"))
            Toast.makeText(Activity_List_NhanVien.this, "Xảy ra lỗi vui lòng thử lại!", Toast.LENGTH_LONG).show();

        if (output.contains("ConnectException")) {
            findViewById(R.id.progress).setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

    }

    public void loaddata() {
        String url = "http://123.123.123.123/ttcn/index.php?task=getlistnhanvien";
        TaskConnect t = new TaskConnect(this, url);
        t.execute();

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(this, Activity_QLNhanVien.class));
    }


}
