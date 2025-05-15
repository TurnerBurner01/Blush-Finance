package com.example.blushfinance.fragments.Pots_Page;

public class Pot {
    private String name;
    private int maxAmount;
    private String type;
    private int iconResId;
    private int colorResId;

    public Pot(String name, int maxAmount, String type, int iconResId, int colorResId) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.type = type;
        this.iconResId = iconResId;
        this.colorResId = colorResId;
    }


    // Getters and setters for the fields
    public String getName() {
        return name;
    }
    public int getMaxAmount() {
        return maxAmount;
    }
    public String getType() {
        return type;
    }
    public int getIconResId() {
        return iconResId;
    }

    public int getColorResId() {
        return colorResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setColorResId(int colorResId) {
        this.colorResId = colorResId;
    }

    public void setName(String name) {
        this.name = name;
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
                ", maxAmount=" + maxAmount +
                ", type='" + type + '\'' +
                '}';
    }
}
