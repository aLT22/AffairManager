package com.bytebuilding.affairmanager.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Alexey on 01.11.2016.
 */

public class DateUtils {

    public static String getDate(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy");

        return simpleDateFormat.format(date);
    }

    public static String getTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        return simpleDateFormat.format(time);
    }
    public static String getFullDate(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy  HH.mm");

        return simpleDateFormat.format(date);
    }

    public static int getMinutePart(long time) {
        return (int) (time % (60 * 60 * 1000)) /(60 * 1000);
    }

    public static int getHours(long time) {
        return (int) time / (60 * 60 * 1000);
    }

    public static String getHHmm(long time){
        String HHmm = "";
        String HH = Integer.toString(getHours(time));
        String mm = Integer.toString(getMinutePart(time));

        HHmm = HH+"ч. "+mm+"м.";

        return HHmm;
    }

}
