package com.eiralv.newtrainglog;

public class Ovelse {

    private int id;
    private String ovelseNavn;

    public Ovelse() {
    }

    public Ovelse(String ovelseNavn) {
        this.ovelseNavn = ovelseNavn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOvelseNavn() {
        return ovelseNavn;
    }

    public void setOvelseNavn(String ovelseNavn) {
        this.ovelseNavn = ovelseNavn;
    }
}
