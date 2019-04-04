package com.ngaoda.php.ThongKe.TimKiemTheoTen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;
import com.ngaoda.php.ThongKe.Class_HoaDon;
import com.ngaoda.php.ThongKe.Lich_Su.Adapter_Lich_Su;
import com.ngaoda.php.ThongKe.Lich_Su.Fragment_thong_tin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_Tim_Kiem_TheoTen extends AppCompatActivity implements AsyncResponse, Adapter_Lich_Su.MyClickListener {
    ArrayList<Class_HoaDon> list=new ArrayList<>();
    Adapter_Lich_Su adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView notresult,tenkh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke_timtheoten_activity__tim__kiem__theo_ten);
        setTitle(Html.fromHtml("<h4>Tìm theo tên khách hàng</h4>"));
        anhxa();
        findViewById(R.id.timkiem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String url="http://123.123.123.123/ttcn/index.php?task=timkiemtheoten";
                    Map<String,String > map=new HashMap<>();
                    if(tenkh.getText().toString().equals("")){
                        map.put("tenkh","Khách vãng lai");
                    }else map.put("tenkh",tenkh.getText().toString());
                    TaskConnect t=new TaskConnect( Activity_Tim_Kiem_TheoTen.this,url );
                    t.setMap( map );
                    t.execute(  );
                    progressBar.setVisibility(View.VISIBLE);
                    notresult.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void anhxa() {
        recyclerView=findViewById(R.id.recy);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new  Adapter_Lich_Su(this,list);
        recyclerView.setAdapter(adapter);
        notresult=findViewById(R.id.notresult);
        progressBar=findViewById(R.id.progress);
        tenkh=findViewById(R.id.tenkh);
    }

    @Override
    public void whenfinish(String output) {
        try{
            if(output.contains("timtheoten")){
                list.clear();
                JSONArray jsonArray=new JSONArray(output.substring(output.indexOf("|")+1));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    JSONArray jsonArray1=new JSONArray(object.getString("mon"));
                    ArrayList<ClassMon> listmon=new ArrayList<>();
                    for(int j=0;j<jsonArray1.length();j++){
                        JSONObject object1=jsonArray1.getJSONObject(j);
                        listmon.add(new ClassMon(object1.getString("mamon"),object1.getString("tenmon"),Integer.parseInt(object1.getString("sl")),
                                Integer.parseInt(object1.getString("gia"))));

                    }
                    list.add(new Class_HoaDon(listmon,object.getString("mahd"),object.getString("tenban")
                            ,object.getString("tenkh")
                            ,object.getString("khachtra")
                            ,object.getString("ghichu")
                            ,object.getString("tennvdat")
                            ,object.getString("tennvtt")
                            ,object.getString("ngay")
                            ,object.getString("thoigian")));

                }

                adapter.notifyDataSetChanged();
            }
            if(output.contains("error")) Toast.makeText(this,"Có lỗi xảy ra vui lòng thử lại!",Toast.LENGTH_LONG).show();
            if(list.size()>0){
                findViewById(R.id.content).setVisibility(View.VISIBLE);
                notresult.setVisibility(View.INVISIBLE);
            }else {
                findViewById(R.id.content).setVisibility(View.INVISIBLE);
                notresult.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.INVISIBLE);

        } catch (JSONException e){
            Toast.makeText(this,"LOL:"+e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnItemClicked(int position) {
        FragmentManager fragmentManager =getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment_thong_tin fr=new Fragment_thong_tin();
        Bundle bundle=new Bundle();
        bundle.putSerializable("tt",list.get(position));
        fr.setArguments(bundle);
        fragmentTransaction.add(R.id.frame,fr);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager =getFragmentManager();
        Fragment fragmentA = fragmentManager.findFragmentById(R.id.frame);
        if (fragmentA == null) {
            super.onBackPressed();
        }
        else{
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentA);
            fragmentTransaction.commit();

        }
    }
}
