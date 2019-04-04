package com.ngaoda.php.ThongKe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;

import com.ngaoda.php.R;
import com.ngaoda.php.ThongKe.Lich_Su.Activity_Lich_Su;
import com.ngaoda.php.ThongKe.Nhan_Vien.Activity_Theo_Nhan_Vien;
import com.ngaoda.php.ThongKe.Theo_Mon.Activity_Theo_Mon;
import com.ngaoda.php.ThongKe.TimKiemTheoTen.Activity_Tim_Kiem_TheoTen;

public class Activity_List_Thongke extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke_activity_list_thongke);
        setTitle(Html.fromHtml("<h3>Thống kê</h3>"));
    }
    public  void  click(View v){
        int id=v.getId();
        switch (id){
            case R.id.lichsu :
                startActivity(new Intent(Activity_List_Thongke.this, Activity_Lich_Su.class));
            break;
            case R.id.theomon:
                startActivity(new Intent(Activity_List_Thongke.this, Activity_Theo_Mon.class));
             break;
            case R.id.theonhanvien:
                startActivity(new Intent(Activity_List_Thongke.this, Activity_Theo_Nhan_Vien.class));
                break;
            case R.id.timkh:
                startActivity(new Intent(Activity_List_Thongke.this, Activity_Tim_Kiem_TheoTen.class));
                break;
        }
    }
}
