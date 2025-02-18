package com.example.blushfinance.fragments.Pots_Page;

public class PotFactory {
    public static Pot createPot(String type, String name, String color, int maxAmount) {
        switch (type) {
            default:
                return new Pot(name, color, maxAmount, "Default");
        }
    }
}
