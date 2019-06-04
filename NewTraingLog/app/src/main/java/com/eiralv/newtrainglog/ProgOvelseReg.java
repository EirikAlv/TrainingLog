package com.eiralv.newtrainglog;

public class ProgOvelseReg {

    private String programNavn;
    private String ovelseNavn;


    public ProgOvelseReg() {
    }

    public ProgOvelseReg(String programNavn, String ovelseNavn) {
        this.programNavn = programNavn;
        this.ovelseNavn = ovelseNavn;
    }

    public String getProgramNavn() {
        return programNavn;
    }

    public void setProgramNavn(String programNavn) {
        this.programNavn = programNavn;
    }

    public String getOvelseNavn() {
        return ovelseNavn;
    }

    public void setOvelseNavn(String ovelseNavn) {
        this.ovelseNavn = ovelseNavn;
    }
}
