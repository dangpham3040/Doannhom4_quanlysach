package com.example.doannhom4_quanlythuvien.model;

public class Statisticals {
    String ten;
    int Tongsosao;
    int mau;

    public Statisticals() {
    }

    public Statisticals(String ten, int tongsosao, int mau) {
        this.ten = ten;
        Tongsosao = tongsosao;
        this.mau = mau;
    }

    public int getMau() {
        return mau;
    }

    public void setMau(int mau) {
        this.mau = mau;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getTongsosao() {
        return Tongsosao;
    }

    public void setTongsosao(int tongsosao) {
        Tongsosao = tongsosao;
    }

    @Override
    public String toString() {
        return "Thongke{" +
                ten +
                ", " + Tongsosao +
                '}';
    }
}
