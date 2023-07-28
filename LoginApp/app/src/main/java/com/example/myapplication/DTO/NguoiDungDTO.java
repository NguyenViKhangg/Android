package com.example.myapplication.DTO;

public class NguoiDungDTO {
    public String hoTen, sdt;

    public NguoiDungDTO() {}

    public NguoiDungDTO(String hoTen, String sdt) {
        this.hoTen = hoTen;
        this.sdt = sdt;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
