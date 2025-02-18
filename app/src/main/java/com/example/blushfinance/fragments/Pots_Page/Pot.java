package com.example.blushfinance.fragments.Pots_Page;

public class Pot {
    private String name;
    private String color;
    private int maxAmount;
    private String type;

    public Pot(String name, String color, int maxAmount, String type) {
        this.name = name;
        this.color = color;
        this.maxAmount = maxAmount;
        this.type = type;
    }

    // Getters and setters for the fields
    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }
    public int getMaxAmount() {
        return maxAmount;
    }
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Pot{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", maxAmount=" + maxAmount +
                ", type='" + type + '\'' +
                '}';
    }
}
