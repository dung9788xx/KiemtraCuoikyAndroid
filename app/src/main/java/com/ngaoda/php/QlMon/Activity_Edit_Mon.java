package com.ngaoda.php.QlMon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import java.util.HashMap;
import java.util.Map;

public class Activity_Edit_Mon extends AppCompatActivity implements AsyncResponse {
    EditText tenmon,gia;
    ClassMon mon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlmon_activity__edit__mon);
        setTitle(Html.fromHtml("<h3>Sửa thông tin món</h3>"));
        anhxa();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            mon=(ClassMon) bundle.getSerializable("mon");
            tenmon.setText(mon.getTenmon());
            gia.setText(mon.getGia()+"");
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
                if(!tenmon.getText().toString().equals("")){
                    if(!gia.getText().toString().equals("")){
                        String url = "http://123.123.123.123/ttcn/index.php?task=editmon";
                        Map<String,String > map=new HashMap<>();
                        map.put("mamon",mon.getMamon());
                        map.put( "tenmon",tenmon.getText().toString());
                        map.put("gia",gia.getText().toString());
                        TaskConnect t = new TaskConnect( Activity_Edit_Mon.this, url);
                        t.setMap( map );
                        t.execute();
                    }else gia.setError("Không được bỏ trống!");
                }else tenmon.setError("Không được bỏ trống!");
            }
        });
    }

    private void anhxa() {
        tenmon=findViewById(R.id.tenmon);
        gia=findViewById(R.id.gia);
    }

    @Override
    public void whenfinish(String output) {
        if(output.contains("edited")){
            Toast.makeText(Activity_Edit_Mon.this,"Cập nhật thành công",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Activity_Edit_Mon.this,Activity_List_Mon.class));
            finish();
        }
        if(output.contains("error")){
            Toast.makeText(Activity_Edit_Mon.this,"Xảy ra lỗi vui lòng thử lại!"+output,Toast.LENGTH_LONG).show();
        }
    }
}
