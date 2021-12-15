package com.example.doannhom4_quanlythuvien.model;

public class Thongke {
    String ten;
    int Tongsosao;

    public Thongke() {
    }

    public Thongke(String ten, int tongsosao) {
        this.ten = ten;
        Tongsosao = tongsosao;
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
