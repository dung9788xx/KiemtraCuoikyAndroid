package com.ngaoda.php.QlDat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AdminActivity;
import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.MainActivity;
import com.ngaoda.php.QLNhanVien.ClassNhanVien;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_Table_State extends AppCompatActivity implements AsyncResponse,TableStateAdapter.MyClickListener {
    public String url="http://123.123.123.123/ttcn/index.php?task=gettablestate";
    static int isadmin;
    ArrayList<ClassTable> list=new ArrayList<>(  );
    TableStateAdapter adapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Handler handler;
    Runnable r;
    ClassNhanVien nhanVien=new ClassNhanVien(  );
   static String username="";
   static boolean showusername;
    TextView tv;
    int checkexit=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_table_state );
        setTitle(Html.fromHtml("<h3>Trạng thái các bàn</h3>"));
        intView();

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            isadmin=bundle.getInt("isadmin");
            username=bundle.getString( "username" );
            showusername=bundle.getBoolean("showusername");
            if(showusername==true){
                findViewById(R.id.text).setVisibility(View.VISIBLE);
                tv.setText(username);
            }
        }
        loadmanv();

        progressBar=(ProgressBar) findViewById( R.id.progress );
        TaskConnect t=new TaskConnect( this,url );
        t.execute(  );
          handler=new Handler(  );
       r=new Runnable() {
            @Override
            public void run() {
                  TaskConnect t=new TaskConnect( Activity_Table_State.this,url );
                  t.execute(  );
                  handler.postDelayed( this,2000 );


            }
        };
        handler.postDelayed( r,2000 );

    }
    public  void intView(){
        tv=(TextView) findViewById(R.id.username);
         recyclerView=(RecyclerView) findViewById( R.id.re );
        recyclerView.setLayoutManager( new GridLayoutManager( this,2 ) );
        adapter=new TableStateAdapter( this,list);
        recyclerView.setAdapter( adapter);

    }

    @Override
    public void whenfinish(String output) {

       if(!output.isEmpty()){
           try{

               if(output.contains( "manv" )) {

                   nhanVien.setManv( output.substring( 4 , output.indexOf( "|" )) );
                   nhanVien.setHoten( output.substring( output.indexOf( "|" )+1 ) );
                   nhanVien.setUsername( username );
               }
           if(output.contains("gettablestate")){
               JSONArray jsonArray = new JSONArray( output.substring(output.indexOf("|")+1));
               list.clear();
               for (int i = 0; i < jsonArray.length(); i++) {
                   JSONObject object = jsonArray.getJSONObject( i );
                   ClassTable c = new ClassTable();
                   if(object.getString("trangthai").equals("1")){
                   c.maban = object.getString( "maban" );
                   c.tenban = object.getString( "tenban" );
                   c.state = object.getString( "busy" );
                   list.add( c );}
               }
               progressBar.setVisibility( View.GONE );
               recyclerView.setVisibility( View.VISIBLE );
               tv.setVisibility(View.VISIBLE);
           }

           }catch (JSONException e){

           }
       }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        if(isadmin==1){
            super.onBackPressed();
            handler.removeCallbacks(r);
            startActivity(new Intent(Activity_Table_State.this, AdminActivity.class));
            finishAffinity();
        }else    {
            checkexit++;
            if(checkexit==2){
                startActivity(new Intent(this, MainActivity.class));
            } else Toast.makeText(this,"Nhấn lần nữa để đăng xuất",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void OnItemClicked(int position) {
     if(list.get(position).state.toString().equals("1")){
         Intent t=new Intent(  Activity_Table_State.this, Activity_Pay_Info.class );
         t.putExtra( "ban",list.get( position ) );
         t.putExtra( "nhanvien",nhanVien );
         startActivity( t);
      //   finish();
     }else{
         Intent t=new Intent(  Activity_Table_State.this, Activity_Book_Info.class );
         t.putExtra( "ban",list.get( position ) );
         t.putExtra( "nhanvien",nhanVien );
         startActivity( t);
       //  finish();
     }
    }
    public  void loadmanv(){
        String url1="http://123.123.123.123/ttcn/index.php?task=getmanv";
        Map<String,String > map=new HashMap<>(  );
        map.put( "username",username );
        TaskConnect t=new TaskConnect( Activity_Table_State.this,url1 );
        t.setMap( map );
        t.execute(  );
    }
}
