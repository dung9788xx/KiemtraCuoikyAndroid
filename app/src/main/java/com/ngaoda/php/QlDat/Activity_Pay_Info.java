package com.ngaoda.php.QlDat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ngaoda.php.AsyncResponse;
import com.ngaoda.php.MoneyType;
import com.ngaoda.php.QLNhanVien.ClassNhanVien;
import com.ngaoda.php.R;
import com.ngaoda.php.TaskConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Pay_Info extends Activity implements AsyncResponse {
    Spinner spinner;
    ClassTable ban;
    ListView listView;
    List<ClassMon> listmon;
    List<ClassMon> booking_food_details ;
    ArrayAdapter<ClassMon> booking_food_detailArrayAdapter;
    LinearLayout loadlayout,booklayout;
    TextView tvtenban,nvdat,tongtien;
    ClassNhanVien nhanVien;
    EditText sl,ghichu,tenkh;
    int tongtienint;
    Bundle bundle;
     ArrayList<ClassTable> tb=new ArrayList<>();
     int positiontablechangto=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__pay);
        anhxa();
        bundle=getIntent().getExtras();
        if(bundle!=null){
            ban=(ClassTable)bundle.getSerializable( "ban" );
            tvtenban.setText( ban.tenban );
            nhanVien=(ClassNhanVien) bundle.getSerializable( "nhanvien" );
        }
        loadmondadat();
        loadmon();

        booking_food_detailArrayAdapter=new ArrayAdapter<ClassMon>( this,R.layout.booking_item,booking_food_details ){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater inflater=(LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                View view=inflater.inflate( R.layout.booking_item,null );
                TextView tenmon=(TextView) view.findViewById( R.id.tenmon );
                TextView sl=(TextView) view.findViewById( R.id.sl );
                TextView tongtien=(TextView) view.findViewById( R.id.tongtien );
                ClassMon f=booking_food_details.get( position );
                tenmon.setText( f.getTenmon() );
                sl.setText( f.getSl()+"" );
                tongtien.setText( MoneyType.toMoney((f.getSl()*f.getGia())) );
                return view;
            }
        };
        listView.setAdapter( booking_food_detailArrayAdapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Pay_Info.this);
                builder.setMessage("Xoá món ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        booking_food_details.remove(position);
                        booking_food_detailArrayAdapter.notifyDataSetChanged();
                        updatetongtien();
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
            }
        });
        findViewById(R.id.them).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sl.getText().toString().equals("")){
                    if(Integer.parseInt(sl.getText().toString())>0){
                        if (booking_food_details.size() < 1) {

                            ClassMon m = (ClassMon) spinner.getSelectedItem();
                            ClassMon f = new ClassMon( m.mamon, m.tenmon, Integer.parseInt( sl.getText().toString() ),
                                    m.gia );
                            booking_food_details.add( f );
                            booking_food_detailArrayAdapter.notifyDataSetChanged();

                        } else {
                            ClassMon m = (ClassMon) spinner.getSelectedItem();
                            boolean isadded=false;//check da co mon do trong ds chua;
                            for (int i = 0; i < booking_food_details.size(); i++) {
                                if (booking_food_details.get( i ).mamon.equals( m.getMamon())) {
                                    ClassMon f = booking_food_details.get( i );
                                    f.sl = f.sl + Integer.parseInt( sl.getText().toString() );
                                    booking_food_details.set( i, f );
                                    booking_food_detailArrayAdapter.notifyDataSetChanged();
                                    isadded=true;
                                }
                            }
                            if(isadded==false){
                                ClassMon f = new ClassMon( m.mamon, m.tenmon, Integer.parseInt( sl.getText().toString() ),
                                        m.gia );
                                booking_food_details.add( f );
                                booking_food_detailArrayAdapter.notifyDataSetChanged();
                            }

                        }
                        updatetongtien();

                    }
                }else Toast.makeText(Activity_Pay_Info.this,"Vui lòng nhập số lượng!",Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tenkh.getText().toString().equals("")&&booking_food_details.size()>0){
                    updatebook();
                }else {
                    Toast.makeText( Activity_Pay_Info.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG ).show();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.cb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadavailabletable();
            }
        });
        findViewById(R.id.thanhtoan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    thanhtoan();
            }
        });

    }

    private void thanhtoan() {
        if(booking_food_details.size()>0){
            AlertDialog dialogBuilder = new AlertDialog.Builder(Activity_Pay_Info.this).create();
            LayoutInflater inflater = (LayoutInflater) Activity_Pay_Info.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.alertdialog_pay, null);
            TextView tt=(TextView)dialogView.findViewById(R.id.tt1);
            Button bt=(Button)dialogView.findViewById(R.id.thanhtoan);
            final EditText khachtra=dialogView.findViewById(R.id.khachtra);
            final TextView tralai=dialogView.findViewById(R.id.tralai);
            tt.setText(tongtien.getText().toString());
            khachtra.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals("")){
                        try{
                            if(Integer.parseInt(s.toString())-tongtienint>0){
                                tralai.setText(MoneyType.toMoney(Integer.parseInt(s.toString())-tongtienint));
                            }else tralai.setText("0");
                        }catch (NumberFormatException e){
                        }
                    }else tralai.setText("0");

                }
            });
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!khachtra.getText().toString().equals("")){
                        if(Integer.parseInt(khachtra.getText().toString())-tongtienint>=0){
                            String url="http://123.123.123.123/ttcn/index.php?task=thanhtoan";
                            TaskConnect t=new TaskConnect(Activity_Pay_Info.this,url);
                            Map<String,String > map=new HashMap<>(  );
                            map.put( "maban",ban.maban );
                            map.put( "khachtra",khachtra.getText().toString());
                            map.put("manvtt",nhanVien.getManv());
                            t.setMap(map);
                            t.execute();
                        }else {
                            khachtra.setError("Số tiền không đúng!");
                        }} else khachtra.setError("Không được bỏ trống!");
                }
            });
            dialogBuilder.setView(dialogView);
            dialogBuilder.show();
         } else Toast.makeText(this,"Vui lòng chọn món",Toast.LENGTH_LONG).show();

    }

    private void loadavailabletable() {
        String url="http://123.123.123.123/ttcn/index.php?task=gettablestate";
        TaskConnect t=new TaskConnect(this,url);
        t.execute();
    }

    private  void updatebook() {
        String url="http://123.123.123.123/ttcn/index.php?task=editbooking";
        TaskConnect t=new TaskConnect(this,url);
        Map<String,String > map=new HashMap<>(  );
        map.put( "maban",ban.maban );
        map.put( "tenkh",tenkh.getText().toString());
        map.put("manv",nhanVien.getManv());
        String gt="";
        if(ghichu.getText().toString().equals("")) gt="";else gt=ghichu.getText().toString();
        map.put("ghichu",gt);
        t.setMap(map);
        t.execute();
    }

    public void updatetongtien() {
        int tong=0;
        for(ClassMon f:booking_food_details){
            tong=tong+(f.getSl()*f.gia);

        }
        tongtienint=tong;
        tongtien.setText(MoneyType.toMoney(tong));
    }
    public  void anhxa(){
        booklayout=findViewById( R.id.booklayout );
        loadlayout=findViewById( R.id.loadlayout );
        spinner=(Spinner) findViewById( R.id.spinner );
        listView=(ListView) findViewById( R.id.lv);
        tvtenban=(TextView) findViewById( R.id.tenban );
        nvdat=(TextView) findViewById( R.id.nvdat );
        tongtien=(TextView) findViewById(R.id.tt);
        listmon=new ArrayList<>(  );
        booking_food_details=new ArrayList<>(  );
        tenkh=(EditText) findViewById(R.id.tenkh);
        ghichu=(EditText) findViewById(R.id.ghichu);
        sl=findViewById(R.id.sl);

    }

    public void loadmon() {
        booklayout.setVisibility(View.GONE);
        String url = "http://123.123.123.123/ttcn/index.php?task=getallmon";
        TaskConnect t = new TaskConnect(this, url);
        t.execute();
    }
    public void loadmondadat() {

        String url = "http://123.123.123.123/ttcn/index.php?task=getbooking";
        TaskConnect t = new TaskConnect(this, url);
        Map<String,String> map=new HashMap<>();
        map.put("maban",ban.maban);
        t.setMap(map);
        t.execute();
    }
    @Override
    public void whenfinish(String output) {
        try{

            if(output.contains("getmon")){
                JSONArray array=new JSONArray( output.substring(output.indexOf("|")+1) );
                for(int i=0;i<array.length();i++){
                    JSONObject object =array.getJSONObject( i );
                    ClassMon m=new ClassMon(  );
                    m.setMamon(object.getString( "mamon" ));
                    m.setTenmon( object.getString( "tenmon" ));
                    m.setGia( object.getInt( "gia" ));
                    listmon.add( m );

                }
                ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.item_mon_an,listmon  ){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater layoutInflater=(LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                        View view=layoutInflater.inflate( R.layout.item_mon_an,null );
                        TextView tenmon=(TextView) view.findViewById( R.id.tenmon );
                        TextView gia=(TextView) view.findViewById( R.id.gia );
                        ClassMon m=listmon.get( position );
                        gia.setText(MoneyType.toMoney( m.getGia())+" VND" );
                        tenmon.setText( m.getTenmon() );
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater layoutInflater=(LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                        View view=layoutInflater.inflate( R.layout.item_mon_an,null );
                        TextView tenmon=(TextView) view.findViewById( R.id.tenmon );
                        TextView gia=(TextView) view.findViewById( R.id.gia );
                        ClassMon m=listmon.get( position );
                        tenmon.setText( m.getTenmon() );
                        gia.setText( MoneyType.toMoney( m.getGia())+" VND" );
                        gia.setVisibility( View.VISIBLE );

                        return view;
                    }
                };
                spinner.setAdapter( arrayAdapter );
                loadlayout.setVisibility( View.GONE );
                booklayout.setVisibility( View.VISIBLE );
            }
            if(output.contains("getbooking")){
                JSONArray array=new JSONArray( output.substring(output.indexOf("|")+1) );
                for(int i=0;i<array.length();i++){
                    JSONObject object =array.getJSONObject( i );
                    ClassMon t=new ClassMon();
                    t.mamon=object.getString("mamon");
                    t.tenmon=object.getString("tenmon");
                    t.sl=Integer.parseInt(object.getString("sl"));
                    t.gia=Integer.parseInt(object.getString("gia"));
                    booking_food_details.add(t);

                }
                JSONObject object =array.getJSONObject( 0);
                nvdat.setText(object.getString("tennv"));
                tenkh.setText(object.getString("tenkh"));
                ghichu.setText(object.getString("ghichu"));
                updatetongtien();
                booking_food_detailArrayAdapter.notifyDataSetChanged();

            }
            if(output.contains("editedbooking")){
                editbookingdetail();
            }
            if (output.contains("done")){
                Toast.makeText(Activity_Pay_Info.this,"Cập nhật thành công",Toast.LENGTH_LONG).show();
                finish();
            }
            if(output.contains("gettablestate")){

                tb.clear();
                JSONArray array=new JSONArray( output.substring(output.indexOf("|")+1) );
                for(int i=0;i<array.length();i++) {
                    JSONObject object = array.getJSONObject(i);
                    if(object.getString("busy").equals("0")&&object.getString("trangthai").equals("1")){
                        ClassTable c=new ClassTable();
                        c.tenban=object.getString("tenban");
                        c.maban=object.getString("maban");
                        tb.add(c);

                    }

                }

                final ArrayAdapter<ClassTable> arrayAdapter = new ArrayAdapter<ClassTable>(Activity_Pay_Info.this,R.layout.available_table_layout_item,tb){
                    @Override
                    public View getView(int position,  View convertView,  ViewGroup parent) {
                        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                        View v=layoutInflater.inflate(R.layout.available_table_layout_item,null);
                        ClassTable t=tb.get(position);
                        TextView tv=v.findViewById(R.id.tv);
                        tv.setText(t.tenban);
                        return v;
                    }
                };
                final AlertDialog dialogBuilder = new AlertDialog.Builder(Activity_Pay_Info.this).create();
                LayoutInflater inflater = (LayoutInflater) Activity_Pay_Info.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.available_table_layout, null);

                final ListView lv = (ListView) dialogView.findViewById(R.id.lv);
                lv.setAdapter(arrayAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        positiontablechangto=position;
                        String url="http://123.123.123.123/ttcn/index.php?task=changetable";
                        TaskConnect t=new TaskConnect(Activity_Pay_Info.this,url);
                        Map<String,String > map=new HashMap<>(  );
                        map.put("mabancu",ban.maban);
                        map.put("mabanmoi",tb.get(position).maban);
                        t.setMap(map);
                        t.execute();
                        dialogBuilder.dismiss();
                    }
                });
                arrayAdapter.notifyDataSetChanged();

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
            if(output.contains("changedtable")){
                ban.maban=tb.get(positiontablechangto).maban;
                ban.tenban=tb.get(positiontablechangto).tenban;
                tvtenban.setText(ban.tenban);
                Toast.makeText(this,"Đổi bàn thành công", Toast.LENGTH_LONG).show();


            }
            if(output.contains("thanhtoanxong")){
                Toast.makeText(this,"Thanh toán thành công", Toast.LENGTH_LONG).show();
                finish();
            }

            if(output.contains("error"))
                Toast.makeText(this,"Xảy ra lỗi vui lòng thử lại ! "+output, Toast.LENGTH_LONG).show();


        }catch (JSONException e){
            Toast.makeText( this,"OK"+e.toString(),Toast.LENGTH_LONG ).show();
        }
    }

    private void editbookingdetail() {
        JSONArray jsArray = new JSONArray();

        for (ClassMon f:booking_food_details){
            JSONObject a=new JSONObject();
            try{
                a.put( "maban",ban.maban);
                a.put( "mamon",f.getMamon());
                a.put("sl",f.getSl());
                jsArray.put( a );
            }catch (JSONException e){

            }

        }
        String url="http://123.123.123.123/ttcn/index.php?task=editbookingdetail";
        TaskConnect t=new TaskConnect(this,url);
        Map<String,String > map=new HashMap<>(  );
        map.put( "json",jsArray.toString());
        map.put("maban",ban.maban);
        t.setMap(map);
        t.execute();
    }
}
