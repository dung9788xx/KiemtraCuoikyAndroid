package com.ngaoda.php.QlDat;

import java.io.Serializable;

public class ClassMon implements Serializable {
   public String mamon,tenmon;
   public int sl,gia;
    public ClassMon() {
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public ClassMon(String mamon, String tenmon, int gia) {
        this.mamon = mamon;
        this.tenmon = tenmon;
        this.gia = gia;
    }
    public ClassMon(String mamon, String tenmon, int sl,int gia) {
        this.mamon = mamon;
        this.tenmon = tenmon;
        this.sl = sl;
        this.gia=gia;
    }
    public String getMamon() {
        return mamon;
    }

    public void setMamon(String mamon) {
        this.mamon = mamon;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }


    public int getSl() {
        return sl;
    }
}
