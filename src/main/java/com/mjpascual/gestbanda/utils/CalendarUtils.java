package com.mjpascual.gestbanda.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarUtils {

    public static Date getDate(){
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
    }

}
