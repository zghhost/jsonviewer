package com.zghhost.jsonviewer.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author guohua.zhang@zhaopin.com.cn
 * @Date 2019/5/10 20:56
 */
public class CalendarUtil {
    public static String yyyyMMdd = "yyyyMMdd";

    public static Date add(Date date,int field,int amount){
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTime(date);
        c.add(field,amount);
        return c.getTime();
    }

    public static String format(Date date,String format){
        return new SimpleDateFormat(format).format(date);
    }
}
