package com.ngaoda.php;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ngaoda.php.QlDat.Activity_Table_State;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements AsyncResponse{
public  static String url="http://123.123.123.123/ttcn/index.php?task=login";
    Button bt;
    EditText username,password;
    ProgressBar progressBar;
    TaskConnect task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

            bt=(Button) findViewById( R.id.bt );
            username=(EditText) findViewById( R.id.username );
            password=(EditText) findViewById( R.id.password );
            progressBar=(ProgressBar) findViewById( R.id.progress );
            bt.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(username.getText().toString().equals( "" )){
                    username.setError( "Không được bỏ trống" );
                }else if(password.getText().toString().equals( "" )){
                    password.setError( "Không được bỏ trống" );
                }else if(!username.getText().toString().matches("[a-zA-Z0-9 ]*" )) {
                    username.setError( "Không được chứa kí tự đặc biệt" );
                }else{
                        Map<String,String > map=new HashMap<>(  );
                        map.put( "username",username.getText().toString() );
                        map.put( "password",password.getText().toString() );
                        task=new TaskConnect(MainActivity.this,url);
                        task.setMap( map );
                        progressBar.setVisibility( View.VISIBLE );
                        task.execute();
                    }

                }
            } );


    }

    @Override
    public void whenfinish(String output) {
        progressBar.setVisibility( View.GONE );
        if(!output.isEmpty()){
        if(output.contains("loginsuccess")){
            //login success
            String level=output.substring(output.indexOf("|")+1);
            if(level.equals("1")){
                Intent t=new Intent( MainActivity.this,AdminActivity.class);
                t.putExtra( "username",username.getText().toString());

                startActivity(t);
            } else{
                Intent t=new Intent( MainActivity.this, Activity_Table_State.class);
                t.putExtra("isadmin",0);
                t.putExtra("showusername",true);
                t.putExtra( "username",username.getText().toString());

                startActivity(t);
            }


        }
        if(output.contains( "errorpass" )){
            password.setError( "Sai mật khẩu" );
        }
        if(output.contains( "errorusername" )) {
            username.setError( "Sai tên đăng nhập" );
        }
        if(output.contains("ConnectException")) Toast.makeText( this,"Kiểm tra lại kết nối!",Toast.LENGTH_LONG ).show();
    }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
