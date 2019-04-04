package com.ngaoda.php;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.QLNhanVien.Activity_QLNhanVien;
import com.ngaoda.php.QlBan.Activity_List_Ban;
import com.ngaoda.php.QlDat.Activity_Table_State;
import com.ngaoda.php.QlMon.Activity_List_Mon;
import com.ngaoda.php.ThongKe.Activity_List_Thongke;

public class AdminActivity extends Activity{
    public  static String url="http://123.123.123.123/ttcn/index.php";
    TextView  tvusername;
   public static String username;
    int checkexit=0;
    int checkexit1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin );
        tvusername=(TextView) findViewById( R.id.username );
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
          username=bundle.getString( "username" );
        }
        tvusername.setText(username );

    }
    public void click(View v){
        int id=v.getId();
        switch (id){
            case R.id.qlnv:
                startActivity( new Intent( AdminActivity.this, Activity_QLNhanVien.class ) );

                break;
            case R.id.dat:
                Intent t=new Intent(  AdminActivity.this, Activity_Table_State.class );
                t.putExtra("isadmin",1);
                t.putExtra( "username",username );
                startActivity( t );
                break;
            case R.id.qlb:
                startActivity(new Intent(AdminActivity.this, Activity_List_Ban.class));
                break;
            case R.id.thongke:
                startActivity(new Intent(AdminActivity.this, Activity_List_Thongke.class));
                break;
            case R.id.qlmon:
                Intent t1=new Intent(  AdminActivity.this, Activity_List_Mon.class );
                startActivity( t1 );
                break;
            case R.id.exit:
                checkexit1++;
                if(checkexit1>=2){
                    startActivity(new Intent(this,MainActivity.class));
                }else   Toast.makeText(AdminActivity.this,"Nhấn lần nữa để đăng xuất",Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onBackPressed() {
            checkexit++;
            if(checkexit==1){
                Toast.makeText(AdminActivity.this,"Nhấn lần nữa để đăng xuất",Toast.LENGTH_LONG).show();
            }
            if(checkexit==2){
               startActivity(new Intent(this,MainActivity.class));
            }
    }
}
