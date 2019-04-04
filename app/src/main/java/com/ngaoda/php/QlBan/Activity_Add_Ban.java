package com.ngaoda.php.QlBan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import java.util.HashMap;
import java.util.Map;

public class Activity_Add_Ban extends AppCompatActivity implements AsyncResponse {
    EditText tenban;
    RadioButton hoadong,baotri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlban_activity__add__ban);
        setTitle("Nhập thông tin bàn");
        tenban=findViewById(R.id.tenban);
        hoadong=findViewById(R.id.hoatdong);
        baotri=findViewById(R.id.baotri);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.them).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tenban.getText().toString().equals("")){
                    if(hoadong.isChecked()||baotri.isChecked()){
                        String url = "http://123.123.123.123/ttcn/index.php?task=addban";
                        TaskConnect t = new TaskConnect(Activity_Add_Ban.this, url);
                        Map<String,String> map=new HashMap<>();
                        map.put("tenban",tenban.getText().toString());
                        if(hoadong.isChecked())    map.put("trangthai","1");
                            else  map.put("trangthai","0");
                        t.setMap(map);
                        t.execute();
                    } else Toast.makeText(Activity_Add_Ban.this,"Chọn trạng thái bàn",Toast.LENGTH_LONG).show();

                }else tenban.setError("Không được bỏ trống");
            }
        });

    }

    @Override
    public void whenfinish(String output) {
        if(output.contains("addedban")){
            Toast.makeText(this,"Thêm bàn thành công",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,Activity_List_Ban.class));
            finishAffinity();
        }
        if(output.contains("error")){
            Toast.makeText(this,"Xảy ra lỗi vui lòng thử lại!"+output,Toast.LENGTH_LONG).show();
        }
    }
    public void onclick(View v){
        int id=v.getId();
        if(id==R.id.hoatdong){
            baotri.setChecked(false);
        }else hoadong.setChecked(false);
    }
}
