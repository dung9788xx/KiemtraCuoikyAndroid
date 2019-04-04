package com.ngaoda.php.QlDat;

import java.io.Serializable;

public class ClassTable implements Serializable {

    String maban,tenban,state;


    public ClassTable(String maban, String tenban, String state) {
        this.maban = maban;
        this.tenban = tenban;
        this.state = state;
    }




    public ClassTable(String maban, String tenban) {
        this.maban = maban;
        this.tenban = tenban;
    }

    public ClassTable() {
    }
    public String getMaban() {
        return maban;
    }

    public void setMaban(String maban) {
        this.maban = maban;
    }

    public String getTenban() {
        return tenban;
    }

    public void setTenban(String tenban) {
        this.tenban = tenban;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
