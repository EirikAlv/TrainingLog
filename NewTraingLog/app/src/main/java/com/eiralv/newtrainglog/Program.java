package com.eiralv.newtrainglog;

public class Program {

    private int id;
    private String programNavn;

    public Program(){}

    public Program(String programNavn) {
        this.programNavn = programNavn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgramNavn() {
        return programNavn;
    }

    public void setProgramNavn(String programNavn) {
        this.programNavn = programNavn;
    }
}
