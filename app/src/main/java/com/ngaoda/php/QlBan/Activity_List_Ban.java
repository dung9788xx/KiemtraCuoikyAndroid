package com.ngaoda.php.QlBan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AdminActivity;
import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QlDat.ClassTable;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_List_Ban extends AppCompatActivity implements AsyncResponse {
    ListView lv;
    ArrayList<ClassTable> listban = new ArrayList<>();
    ArrayAdapter<ClassTable> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlban_activity__list__ban);
        setTitle(Html.fromHtml("<h3>Danh sách bàn</h3>"));

        anhxa();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addmon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_List_Ban.this, Activity_Add_Ban.class));
            }
        });
        loadban();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_List_Ban.this);
                builder.setMessage("Xác nhận xóa bàn?");
                builder.setCancelable(false);
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = "http://123.123.123.123/ttcn/index.php?task=deleteban";
                        Map<String,String > map=new HashMap<>();
                        map.put( "maban",listban.get( position).getMaban());
                        TaskConnect t = new TaskConnect( Activity_List_Ban.this, url);
                        t.setMap( map );
                        t.execute();
                        loadban();
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
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent t=new Intent(Activity_List_Ban.this, Activity_Edit_Ban.class);
                t.putExtra("ban",listban.get(position));
                startActivity(t);
            }
        });
    }

    private void anhxa() {
        lv=(ListView) findViewById(R.id.lv);
    }

    private void loadban() {
            String url = "http://123.123.123.123/ttcn/index.php?task=gettablestate";
            TaskConnect t = new TaskConnect(this, url);
            t.execute();



    }

    @Override
    public void whenfinish(String output) {
        try {
            if(output.contains("gettablestate")){
                listban.clear();
                JSONArray jsonArray=new JSONArray(output.substring(output.indexOf("|")+1));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    listban.add(new ClassTable(object.getString("maban"),object.getString("tenban"),object.getString("trangthai")));
                }
                arrayAdapter=new ArrayAdapter<ClassTable>(Activity_List_Ban.this,R.layout.qlban_item_ban,listban){
                    @Override
                    public View getView(int position,View convertView, ViewGroup parent) {
                        LayoutInflater layoutInflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view=layoutInflater.inflate(R.layout.qlban_item_ban,null);
                        ImageView imageView=view.findViewById(R.id.active);
                        TextView tenban=view.findViewById(R.id.tenban);
                        ClassTable tb=listban.get(position);
                        if(tb.getState().equals("1")){
                            imageView.setImageResource(R.drawable.ic_isactive);
                        }else imageView.setImageResource(R.drawable.ic_deactive);
                        tenban.setText(tb.getTenban());
                        return view;
                    }
                };
                lv.setAdapter(arrayAdapter);
                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.content).setVisibility(View.VISIBLE);


            }
            if(output.contains("using")){
                Toast.makeText(this,"Bàn đang được sử dụng vui lòng thanh toán !",Toast.LENGTH_LONG).show();
            }
            if(output.contains("deleted")){
                Toast.makeText(this,"Xóa thành công",Toast.LENGTH_LONG).show();
            }
            if(output.contains("error")){
                Toast.makeText(this,"Xảy ra lỗi vui lòng thử lại!",Toast.LENGTH_LONG).show();
            }

        }catch (JSONException e){

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AdminActivity.class));
        finishAffinity();

    }
}
