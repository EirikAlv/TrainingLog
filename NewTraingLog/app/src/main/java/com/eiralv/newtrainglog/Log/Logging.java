package com.eiralv.newtrainglog.Log;

import java.time.LocalDate;

public class Logging {

    private int id;
    private String programNavn;
    private String ovelseNavn;
    private String vekt;
    private String reps;
    private String dato;
    private String mesure;

    public Logging() {
    }

    public Logging(String programNavn, String ovelseNavn, String vekt, String reps, String mesure) {
        this.programNavn = programNavn;
        this.ovelseNavn = ovelseNavn;
        this.vekt = vekt;
        this.reps = reps;
        this.mesure = mesure;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            this.dato = LocalDate.now().toString();
        }
    }

    public String getMesure() {
        return mesure;
    }

    public void setMesure(String mesure) {
        this.mesure = mesure;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
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

    public String getOvelseNavn() {
        return ovelseNavn;
    }

    public void setOvelseNavn(String ovelseNavn) {
        this.ovelseNavn = ovelseNavn;
    }

    public String getVekt() {
        return vekt;
    }

    public void setVekt(String vekt) {
        this.vekt = vekt;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }
}
