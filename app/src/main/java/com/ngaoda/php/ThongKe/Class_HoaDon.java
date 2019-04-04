package com.ngaoda.php.ThongKe;

import com.ngaoda.php.QlDat.ClassMon;

import java.io.Serializable;
import java.util.ArrayList;

public class Class_HoaDon implements Serializable {
   public   String mahd,tenban,tenkh,khachtra,ghichu,tennvdat,tennvtt,ngay,thoigian;
   public ArrayList<ClassMon> listmon;
    Class_HoaDon (){

    }

    public Class_HoaDon(ArrayList<ClassMon> listmon,String mahd, String tenban, String tenkh, String khachtra, String ghichu, String tennvdat, String tennvtt, String ngay, String thoigian) {
        this.listmon=listmon;
        this.mahd = mahd;
        this.tenban = tenban;
        this.tenkh = tenkh;
        this.khachtra = khachtra;
        this.ghichu = ghichu;
        this.tennvdat = tennvdat;
        this.tennvtt = tennvtt;
        this.ngay = ngay;
        this.thoigian = thoigian;
    }

}
