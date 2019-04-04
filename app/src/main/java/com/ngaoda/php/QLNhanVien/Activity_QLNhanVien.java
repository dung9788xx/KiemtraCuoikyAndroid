package com.ngaoda.php.QLNhanVien;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.ngaoda.php.AdminActivity;
import com.ngaoda.php.QLNhanVien.DanhSach.Activity_List_NhanVien;
import com.ngaoda.php.QLNhanVien.QlChamCong.Activity_Cham_Cong;
import com.ngaoda.php.QLNhanVien.TinhLuong.Activity_TinhLuong;
import com.ngaoda.php.R;

public class Activity_QLNhanVien extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlnhanvien_activity__qlnhan_vien);
        setTitle(Html.fromHtml("<h5>Quản lý nhân viên</h5>"));
    }
    public void click(View view){
        int id=view.getId();

        switch (id){
            case R.id.danhsachnv:
                startActivity(new Intent(this, Activity_List_NhanVien.class));
                break;
            case R.id.chamcong:
                startActivity(new Intent(this, Activity_Cham_Cong.class));
                break;
            case R.id.tinhluong:
                startActivity(new Intent(this, Activity_TinhLuong.class));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AdminActivity.class));
    }


}
