package com.bytebuilding.affairmanager.model;

import com.bytebuilding.affairmanager.R;

public class Separator implements Item{

    public static final int SEPARATOR_TYPE_LOST = R.string.separator_lost;
    public static final int SEPARATOR_TYPE_NOW = R.string.separator_now;
    public static final int SEPARATOR_TYPE_TOMORROW = R.string.separator_tomorrow;
    public static final int SEPARATOR_TYPE_FUTURE = R.string.separator_future;

    private int separatorType;

    public Separator(int separatorType) {
        this.separatorType = separatorType;
    }

    @Override
    public boolean isAffair() {
        return false;
    }

    public int getSeparatorType() {
        return separatorType;
    }

    public void setSeparatorType(int separatorType) {
        this.separatorType = separatorType;
    }
}
