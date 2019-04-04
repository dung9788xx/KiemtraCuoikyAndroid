package com.ngaoda.php.QlMon;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AdminActivity;
import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.MoneyType;
import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_List_Mon extends AppCompatActivity implements AsyncResponse {
    ListView lv;
    ArrayList<ClassMon> listmon = new ArrayList<>();
    ArrayAdapter<ClassMon> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlmon_activity__list__mon);
        setTitle(Html.fromHtml("<h3>Danh sách món</h3>"));
        anhxa();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addmon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_List_Mon.this, Activity_Add_Mon.class));
            }
        });
        loadmon();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent t=new Intent(Activity_List_Mon.this,Activity_Edit_Mon.class);
                    t.putExtra("mon",listmon.get(position));
                    startActivity(t);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_List_Mon.this);

                builder.setMessage("Toàn bộ thông tin về món này sẽ bị xóa ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = "http://123.123.123.123/ttcn/index.php?task=xoamon";
                        Map<String,String > map=new HashMap<>();
                        map.put( "mamon",listmon.get( position).getMamon());
                        TaskConnect t = new TaskConnect( Activity_List_Mon.this, url);
                        t.setMap( map );
                        t.execute();
                        loadmon();
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

    }

    private void anhxa() {
        lv = (ListView) findViewById(R.id.lv);
    }

    private void loadmon() {
        String url = "http://123.123.123.123/ttcn/index.php?task=getallmon";
        TaskConnect t = new TaskConnect(this, url);
        t.execute();

    }

    @Override
    public void whenfinish(String output) {
        findViewById(R.id.progress).setVisibility(View.GONE);
        try{
            if (output.contains("getmon")) {
                listmon.clear();
                JSONArray array = new JSONArray(output.substring(output.indexOf("|") + 1));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    ClassMon m = new ClassMon();
                    m.setMamon(object.getString("mamon"));
                    m.setTenmon(object.getString("tenmon"));
                    m.setGia(object.getInt("gia"));
                    listmon.add(m);

                }
                arrayAdapter=new ArrayAdapter<ClassMon>(this,R.layout.qlmon_item_mon,listmon){
                    @Override
                    public View getView(int position,  View convertView, ViewGroup parent) {
                        LayoutInflater layoutInflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View v=layoutInflater.inflate(R.layout.qlmon_item_mon,null);
                        TextView tenmon=v.findViewById(R.id.tenmon);
                        TextView gia=v.findViewById(R.id.gia);
                        ClassMon m=listmon.get(position);
                        tenmon.setText(m.getTenmon());
                        gia.setText(MoneyType.toMoney(m.getGia()));
                        return v;
                    }
                };
                lv.setAdapter(arrayAdapter);
                findViewById(R.id.content).setVisibility(View.VISIBLE);
            }
            if(output.contains("using"))
                Toast.makeText(Activity_List_Mon.this, "Món đang được sử dụng! ", Toast.LENGTH_LONG).show();
            if(output.contains("deletesuccess")){
                Toast.makeText(Activity_List_Mon.this,"Xóa thành công",Toast.LENGTH_LONG).show();
            }
            if(output.contains("error"))   Toast.makeText(Activity_List_Mon.this,"Xảy ra lỗi vui lòng thử lại !",Toast.LENGTH_LONG).show();
        }catch (JSONException e){

        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_List_Mon.this, AdminActivity.class));
        finishAffinity();

    }
}
