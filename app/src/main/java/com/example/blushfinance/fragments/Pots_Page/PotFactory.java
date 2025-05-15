package com.example.blushfinance.fragments.Pots_Page;

import com.example.blushfinance.R;

public class PotFactory {
    public static Pot createPot(String type, String name, int maxAmount) {
        int iconResId, colorResId;

        switch (type.toLowerCase()) {
            case "holiday":
                iconResId = R.drawable.ic_holiday;
                colorResId = R.color.income_green;
                break;
            case "car":
                iconResId = R.drawable.ic_car;
                colorResId = R.color.balance_blue;
                break;
            default:
                iconResId = R.drawable.pots_icon;
                colorResId = R.color.white;
        }

        return new Pot(name, maxAmount, type, iconResId, colorResId);
    }

}
