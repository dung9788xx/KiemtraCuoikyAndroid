package com.ngaoda.php.QLNhanVien.DanhSach;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QLNhanVien.ClassNhanVien;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Activity_Edit_NhanVien extends AppCompatActivity implements AsyncResponse {
    EditText hoten,ngaysinh,diachi,sdt,username,password,repassword,luong;
    CheckBox checboxklevel;
    ClassNhanVien nv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.qlnv_activity_edit_nhan_vien);
        setTitle(Html.fromHtml("<h5>Sửa thông tin nhân viên</h5>"));
        anhxa();
        setdataforform();
        findViewById( R.id.date ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdatepicker();
            }
        } );
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById( R.id. update).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkformed()){
                    String url="http://123.123.123.123/ttcn/index.php?task=editnhanvien";
                    Map<String,String > map=new HashMap<>(  );
                    map.put( "manv",nv.getManv() );
                    map.put( "hoten",hoten.getText().toString() );
                    map.put( "ngaysinh",ngaysinh.getText().toString() );
                    map.put( "diachi",diachi.getText().toString() );
                    map.put( "sdt",sdt.getText().toString() );
                    map.put( "username",username.getText().toString() );
                    map.put( "password",password.getText().toString() );
                    map.put( "luong",luong.getText().toString() );
                    if(checboxklevel.isChecked()){
                        map.put("level","1");
                    }else map.put("level","0");
                    TaskConnect taskConnect=new TaskConnect( Activity_Edit_NhanVien.this,url );
                    taskConnect.setMap( map );
                    taskConnect.execute(  );
                }
            }
        } );
    }
    public boolean checkformed(){
        boolean status=true;
        if(hoten.getText().toString().equals( "" )) {status=false;hoten.setError( "Không được bỏ trống" );};
        if(ngaysinh.getText().toString().equals( "" )){status=false;ngaysinh.setError( "Không được bỏ trống" );}
        if(diachi.getText().toString().equals( "" )){status=false;diachi.setError( "Không được bỏ trống" );}
        if(sdt.getText().toString().equals( "" )){status=false;sdt.setError( "Không được bỏ trống" );}
        if(username.getText().toString().equals( "" )){status=false;username.setError( "Không được bỏ trống" );}
        if(!username.getText().toString().matches("[a-zA-Z0-9 ]*" )){
            status=false;username.setError( "Không chứa ký tự đặc biệt" );
        }
        if(password.getText().toString().equals( "" )){status=false;password.setError( "Không được bỏ trống" );}
        if(!password.getText().toString().equals( "" )){
            if(!password.getText().toString().equals( repassword.getText().toString())){
                status=false;
                repassword.setError( "Hai mật khẩu không khớp" );
            }
        }
        if(luong.getText().toString().equals( "" )) {status=false;luong.setError( "Không được bỏ trống" );};
        return status;

    }
    public void anhxa(){
        hoten=(EditText) findViewById( R.id.hoten );
        ngaysinh=(EditText) findViewById( R.id.ngaysinh );
        diachi=(EditText) findViewById( R.id.diachi );
        sdt=(EditText) findViewById( R.id.sdt );
        username=(EditText) findViewById( R.id.username );
        password=(EditText) findViewById( R.id.password );
        repassword=(EditText) findViewById( R.id.repassword );
        luong=findViewById(R.id.luong);
        checboxklevel=findViewById(R.id.cb);
    }

    @Override
    public void whenfinish(String output) {
        if(output.contains( "dacouser" )){
            username.setError( "Tài khoản đã tồn tại" );

        }else if(output.contains( "ok" )){
            Toast.makeText( this,"Sửa thành công" ,Toast.LENGTH_LONG).show();
            startActivity( new Intent( Activity_Edit_NhanVien.this, Activity_List_NhanVien.class ) );

        }else  Toast.makeText( this,"Có lỗi xảy ra !/n"+output ,Toast.LENGTH_LONG).show();
    }
    public  void showdatepicker(){
        final Calendar c = Calendar.getInstance();
        String n=nv.getNgaysinh();
        String []s=n.split( "/" );
        int  mYear = Integer.parseInt(  s[2] );
        int mMonth = Integer.parseInt(  s[1] );
        int  mDay =Integer.parseInt(  s[0] );
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        ngaysinh.setText(dayOfMonth + "/" + (monthOfYear +1) + "/" + year);

                    }
                }, mYear, mMonth-1, mDay);
        datePickerDialog.show();
    }
    public  void setdataforform(){
        nv=(ClassNhanVien)getIntent().getSerializableExtra( "nv" );
        hoten.setText( nv.getHoten() );
        ngaysinh.setText( nv.getNgaysinh() );
        diachi.setText( nv.getDiachi() );
        sdt.setText( nv.getSdt() );
        username.setText( nv.getUsername() );
        password.setText( nv.getPassword() );
        repassword.setText( nv.getPassword() );
        luong.setText(nv.getLuong());
       if(nv.level.equals("1")){
           checboxklevel.setChecked(true);
       }else  checboxklevel.setChecked(false);
    }
}
