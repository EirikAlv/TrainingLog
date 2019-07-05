package com.eiralv.newtrainglog.Adapter;

public class ListAdapterItem {

    private String weight;
    private String reps;

    public ListAdapterItem(String weight, String reps) {
        this.weight = weight;
        this.reps = reps;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }
}
