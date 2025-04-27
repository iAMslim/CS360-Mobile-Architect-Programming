package com.zybooks.weighttrackingappui.ui.theme;

public class WeightEntry {
    private int id;
    private String date;
    private float weight;

    public WeightEntry(int id, String date, float weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public float getWeight() {
        return weight;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
