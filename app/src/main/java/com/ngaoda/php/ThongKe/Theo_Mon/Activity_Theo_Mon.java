package com.ngaoda.php.ThongKe.Theo_Mon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Activity_Theo_Mon extends AppCompatActivity implements AsyncResponse, Adapter_Theo_Mon.MyClickListenner {
    EditText edttu, edtden;
    TextView notresult,sl,mamon,temon;
    ArrayList<ClassMon> list=new ArrayList<>();
    Adapter_Theo_Mon adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button bieudo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke_activity__theo__mon);
        setTitle(Html.fromHtml("<h4>Thống kê theo món</h4>"));
        anhxa();
        findViewById(R.id.datepick1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear;
                int mMonth;
                int mDay;
                if (edttu.getText().toString().equals("")) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH)+1;
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    String s[] = edttu.getText().toString().split("/");
                    mYear = Integer.parseInt(s[2]);
                    mMonth = Integer.parseInt(s[1]);
                    mDay = Integer.parseInt(s[0]);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Theo_Mon.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edttu.setText(dayOfMonth + "/" + (monthOfYear+1 ) + "/" + year);
                            }
                        }, mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.datepick2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                int mYear;
                int mMonth;
                int mDay;
                if (edtden.getText().toString().equals("")) {
                    mYear = c1.get(Calendar.YEAR);
                    mMonth = c1.get(Calendar.MONTH)+1;
                    mDay = c1.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] s = edtden.getText().toString().split("/");
                    mYear = Integer.parseInt(s[2]);
                    mMonth = Integer.parseInt(s[1]);
                    mDay = Integer.parseInt(s[0]);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Theo_Mon.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtden.setText(dayOfMonth + "/" + (monthOfYear+1 ) + "/" + year);

                            }
                        }, mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.timkiem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!edtden.getText().toString().equals("")&&!edttu.getText().toString().equals("")){
                    String url="http://123.123.123.123/ttcn/index.php?task=thongketheomon";
                    Map<String,String > map=new HashMap<>();
                    String []s=edttu.getText().toString().split("/");
                    String tungay=s[2]+"/"+s[1]+"/"+s[0];
                    map.put("tungay",tungay);
                    String []s1=edtden.getText().toString().split("/");
                    String dengay=s1[2]+"/"+s1[1]+"/"+s1[0];
                    map.put("denngay",dengay);
                    TaskConnect t=new TaskConnect( Activity_Theo_Mon.this,url );
                    t.setMap( map );
                    t.execute(  );
                    progressBar.setVisibility(View.VISIBLE);
                    notresult.setVisibility(View.INVISIBLE);

                }else Toast.makeText(Activity_Theo_Mon.this,"Vui lòng chon ngày",Toast.LENGTH_LONG).show();
            }
        });
        bieudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t=new Intent(Activity_Theo_Mon.this,Activity_Pie_Chart.class);
                t.putExtra("list",list);
                startActivity(t);
            }
        });
    }

    private void anhxa() {
        edttu = findViewById(R.id.edttungay);
        edtden = findViewById(R.id.edtdenngay);
        sl=findViewById(R.id.sl);
        mamon=findViewById(R.id.mamon);
        temon=findViewById(R.id.tenmon);
        recyclerView=findViewById(R.id.recy);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new Adapter_Theo_Mon(this,list);
        recyclerView.setAdapter(adapter);
        notresult=findViewById(R.id.notresult);
        progressBar=findViewById(R.id.progress);
        bieudo=findViewById(R.id.bieudo);

    }

    @Override
    public void whenfinish(String output) {
        try{
            if(output.contains("thongketheomon")){
                list.clear();
                JSONArray jsonArray=new JSONArray(output.substring(output.indexOf("|")+1));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    list.add(new ClassMon(object.getString("mamon"),object.getString("tenmon"),Integer.parseInt(object.getString("sl")),
                            Integer.parseInt( object.getString("gia"))));

                }
                adapter.notifyDataSetChanged();
            }
            if(output.contains("error")) Toast.makeText(this,"Có lỗi xảy ra vui lòng thử lại!",Toast.LENGTH_LONG).show();

            if(list.size()>0){
                bieudo.setVisibility(View.VISIBLE);
                temon.setVisibility(View.VISIBLE);
                sl.setVisibility(View.VISIBLE);
                mamon.setVisibility(View.VISIBLE);
                notresult.setVisibility(View.GONE);
            }else {
                bieudo.setVisibility(View.GONE);
                temon.setVisibility(View.GONE);
                sl.setVisibility(View.GONE);
                mamon.setVisibility(View.GONE);
                notresult.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.INVISIBLE);

        } catch (JSONException e){
            Toast.makeText(this,"LOL:"+e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnItemClicked(int position) {

    }
}
