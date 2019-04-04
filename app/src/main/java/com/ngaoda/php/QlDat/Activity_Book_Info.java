package com.ngaoda.php.QlDat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Activity_Book_Info extends Activity implements AsyncResponse {
    Spinner spinner;
    ClassTable ban;
    ListView listView;
    List<ClassMon> listmon;
    List<ClassMon> booking_food_details;
    ArrayAdapter<ClassMon> booking_food_detailArrayAdapter;
    LinearLayout loadlayout, booklayout;
    TextView tvtenban, nvdat, tongtien;
    ClassNhanVien nhanVien;
    EditText sl, ghichu, tenkh;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);
        anhxa();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            ban = (ClassTable) bundle.getSerializable("ban");
            tvtenban.setText(ban.tenban);
            nhanVien = (ClassNhanVien) bundle.getSerializable("nhanvien");
            nvdat.setText(nhanVien.getHoten());

        }
        loadmon();
        booking_food_detailArrayAdapter = new ArrayAdapter<ClassMon>(this, R.layout.booking_item, booking_food_details) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.booking_item, null);
                TextView tenmon = (TextView) view.findViewById(R.id.tenmon);
                TextView sl = (TextView) view.findViewById(R.id.sl);
                TextView tongtien = (TextView) view.findViewById(R.id.tongtien);
                ClassMon f = booking_food_details.get(position);
                tenmon.setText(f.getTenmon());
                sl.setText(f.getSl() + "");
                tongtien.setText((MoneyType.toMoney(f.getSl() * f.getGia())));
                return view;
            }
        };
        listView.setAdapter(booking_food_detailArrayAdapter);

        findViewById(R.id.dat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booking_food_details.size() > 0) {
                    dat();
                } else {
                    Toast.makeText(Activity_Book_Info.this, "Vui lòng chọn món!", Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Book_Info.this);
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
        findViewById(R.id.huy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.them).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sl.getText().toString().equals("")) {
                    if (Integer.parseInt(sl.getText().toString()) > 0) {
                        if (booking_food_details.size() < 1) {

                            ClassMon m = (ClassMon) spinner.getSelectedItem();
                            ClassMon f = new ClassMon(m.mamon, m.tenmon, Integer.parseInt(sl.getText().toString()),
                                    m.gia);
                            booking_food_details.add(f);
                            booking_food_detailArrayAdapter.notifyDataSetChanged();

                        } else {
                            ClassMon m = (ClassMon) spinner.getSelectedItem();
                            boolean isadded = false;//check da co mon do trong ds chua;
                            for (int i = 0; i < booking_food_details.size(); i++) {
                                if (booking_food_details.get(i).mamon.equals(m.getMamon())) {
                                    ClassMon f = booking_food_details.get(i);
                                    f.sl = f.sl + Integer.parseInt(sl.getText().toString());
                                    booking_food_details.set(i, f);
                                    booking_food_detailArrayAdapter.notifyDataSetChanged();
                                    isadded = true;
                                }
                            }
                            if (isadded == false) {
                                ClassMon f = new ClassMon(m.mamon, m.tenmon, Integer.parseInt(sl.getText().toString()),
                                        m.gia);
                                booking_food_details.add(f);
                                booking_food_detailArrayAdapter.notifyDataSetChanged();
                            }

                        }
                        updatetongtien();
                        Toast.makeText(Activity_Book_Info.this, "Thêm thành công", Toast.LENGTH_LONG).show();

                    }
                } else
                    Toast.makeText(Activity_Book_Info.this, "Vui lòng nhập số lượng!", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void updatetongtien() {
        int tong = 0;
        for (ClassMon f : booking_food_details) {
            tong = tong + (f.getSl() * f.gia);

        }
        tongtien.setText(MoneyType.toMoney(tong));
    }

    public void anhxa() {
        booklayout = findViewById(R.id.booklayout);
        loadlayout = findViewById(R.id.loadlayout);
        spinner = (Spinner) findViewById(R.id.spinner);
        listView = (ListView) findViewById(R.id.lv);
        tvtenban = (TextView) findViewById(R.id.tenban);
        nvdat = (TextView) findViewById(R.id.nvdat);
        tongtien = (TextView) findViewById(R.id.tt);
        listmon = new ArrayList<>();
        booking_food_details = new ArrayList<>();
        tenkh = (EditText) findViewById(R.id.tenkh);
        ghichu = (EditText) findViewById(R.id.ghichu);
        sl = findViewById(R.id.sl);

    }

    public void loadmon() {
        booklayout.setVisibility(View.GONE);
        String url = "http://123.123.123.123/ttcn/index.php?task=getallmon";
        TaskConnect t = new TaskConnect(this, url);
        t.execute();
    }

    public void dat() {
        String url = "http://123.123.123.123/ttcn/index.php?task=addbooking";
        TaskConnect t = new TaskConnect(this, url);
        Map<String, String> map = new HashMap<>();
        map.put("maban", ban.maban);
        if(tenkh.getText().toString().equals("")){
            map.put("tenkh", "Khách vãng lai");
        }else map.put("tenkh", tenkh.getText().toString());
        map.put("manv", nhanVien.getManv());
        String gt = "";
        if (ghichu.getText().toString().equals("")) gt = "";
        else gt = ghichu.getText().toString();
        map.put("ghichu", gt);
        t.setMap(map);
        t.execute();
    }

    @Override
    public void whenfinish(String output) {
        try {
            if (output.contains("getmon")) {
                JSONArray array = new JSONArray(output.substring(output.indexOf("|") + 1));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    ClassMon m = new ClassMon();
                    m.setMamon(object.getString("mamon"));
                    m.setTenmon(object.getString("tenmon"));
                    m.setGia(object.getInt("gia"));
                    listmon.add(m);

                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_mon_an, listmon) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.item_mon_an, null);
                        TextView tenmon = (TextView) view.findViewById(R.id.tenmon);
                        TextView gia = (TextView) view.findViewById(R.id.gia);
                        ClassMon m = listmon.get(position);
                        gia.setText(MoneyType.toMoney(m.getGia()) + " VND");
                        tenmon.setText(m.getTenmon());
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.item_mon_an, null);
                        TextView tenmon = (TextView) view.findViewById(R.id.tenmon);
                        TextView gia = (TextView) view.findViewById(R.id.gia);
                        ClassMon m = listmon.get(position);
                        tenmon.setText(m.getTenmon());
                        gia.setText(MoneyType.toMoney(m.getGia()) + " VND");
                        gia.setVisibility(View.VISIBLE);

                        return view;
                    }
                };
                spinner.setAdapter(arrayAdapter);
                loadlayout.setVisibility(View.GONE);
                booklayout.setVisibility(View.VISIBLE);

            }
            if (output.contains("addedbooking")) {
                addtobookingdetail();


            }
            if (output.contains("done")) {
                Toast.makeText(this, "Đặt thành công", Toast.LENGTH_LONG).show();
                Intent t = new Intent(Activity_Book_Info.this, Activity_Table_State.class);
                startActivity(t);
                finishAffinity();
            }
            if (output.contains("error"))
                Toast.makeText(this, "Xảy ra lỗi vui lòng thử lại ! " + output, Toast.LENGTH_LONG).show();


        } catch (JSONException e) {
            Toast.makeText(this, "OK" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void addtobookingdetail() {
        JSONArray jsArray = new JSONArray();
        for (ClassMon f : booking_food_details) {
            JSONObject a = new JSONObject();
            try {
                a.put("maban", ban.maban);
                a.put("mamon", f.getMamon());
                a.put("sl", f.getSl());
                jsArray.put(a);
            } catch (JSONException e) {

            }

        }
        String url = "http://123.123.123.123/ttcn/index.php?task=addbookingdetail";
        TaskConnect t = new TaskConnect(this, url);
        Map<String, String> map = new HashMap<>();
        map.put("json", jsArray.toString());
        t.setMap(map);
        t.execute();


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
//        Intent t=new Intent(Activity_Book_Info.this, Activity_Table_State.class);
//        t.putExtra( "username",nhanVien.getUsername());
//        t.putExtra("showusername",bundle.getBoolean("showusername"));
//        startActivity(t);
//        finish();


    }
}
