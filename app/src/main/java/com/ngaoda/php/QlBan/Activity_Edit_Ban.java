package com.ngaoda.php.QlBan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QlDat.ClassTable;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import java.util.HashMap;
import java.util.Map;

public class Activity_Edit_Ban extends AppCompatActivity implements AsyncResponse {
    EditText tenban;
    ClassTable table=new ClassTable();
    RadioButton hoadong,baotri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlban_activity__edit__ban);
        setTitle("Sửa thông tin bàn");
        tenban=findViewById(R.id.tenban);
        hoadong=findViewById(R.id.hoatdong);
        baotri=findViewById(R.id.baotri);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            table=(ClassTable) bundle.getSerializable("ban");
            tenban.setText(table.getTenban());
            if(table.getState().equals("1")) hoadong.setChecked(true); else baotri.setChecked(true);
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.capnhat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tenban.getText().toString().equals("")){
                    if(hoadong.isChecked()||baotri.isChecked()){
                    String url = "http://123.123.123.123/ttcn/index.php?task=editban";
                    TaskConnect t = new TaskConnect(Activity_Edit_Ban.this, url);
                    Map<String,String> map=new HashMap<>();
                    map.put("maban",table.getMaban());
                    map.put("tenban",tenban.getText().toString());
                        if(hoadong.isChecked())    map.put("trangthai","1");
                        else  map.put("trangthai","0");
                        t.setMap(map);
                    t.setMap(map);
                    t.execute();
                    } else Toast.makeText(Activity_Edit_Ban.this,"Chọn trạng thái bàn",Toast.LENGTH_LONG).show();
                }else tenban.setError("Không được bỏ trống");
            }
        });


    }

    @Override
    public void whenfinish(String output) {
        if(output.contains("edited")){
            Toast.makeText(this,"Cập nhật thành công",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,Activity_List_Ban.class));
            finishAffinity();
        }
        if(output.contains("using")){
            Toast.makeText(this,"Bàn đang được sử dụng vui lòng thanh toán !",Toast.LENGTH_LONG).show();
        }
        if(output.contains("error")){
            Toast.makeText(this,"Xảy ra lỗi vui lòng thử!",Toast.LENGTH_LONG).show();
        }
    }
    public void onclick(View v){
        int id=v.getId();
        if(id==R.id.hoatdong){
            baotri.setChecked(false);
        }else hoadong.setChecked(false);
    }
}
