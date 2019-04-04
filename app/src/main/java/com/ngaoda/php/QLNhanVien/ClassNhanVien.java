package com.ngaoda.php.QLNhanVien;

import java.io.Serializable;

public class ClassNhanVien implements Serializable {
    public String manv;
    public String level;
    public String hoten;
    public String ngaysinh;
    public String diachi;
    public String sdt;
    public String username;
    public String password;
    public String luong;
    public String denlam;

    public ClassNhanVien(String manv, String hoten, String ngaysinh,String denlam) {
        this.manv = manv;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.denlam=denlam;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLuong() {
        return luong;
    }

    public void setLuong(String luong) {
        this.luong = luong;
    }

    public ClassNhanVien() {
    }

    public ClassNhanVien(String Manv, String hoten, String ngaysinh, String diachi, String sdt, String username, String password, String level,String luong) {
        this.manv = Manv;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.username = username;
        this.password = password;
        this.level = level;
        this.luong=luong;

    }

    public ClassNhanVien(String manv, String hoten) {
        this.manv = manv;
        this.hoten = hoten;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

}
