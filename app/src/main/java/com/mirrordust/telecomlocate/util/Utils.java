package com.mirrordust.telecomlocate.util;

import com.mirrordust.telecomlocate.entity.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiaoShanhe on 2017/07/25/025.
 */

public class Utils {

    public static String index2String(int index) {
        return String.format("No.%s", index);
    }

    public static String baseStationNum2String(int number) {
        return String.format("[%s BS]", number);
    }

    public static String timestamp2LocalTime(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(milliseconds));
    }

    public static String timestamp2ShortLocalTime(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm", Locale.getDefault());
        return sdf.format(new Date(milliseconds));
    }

    public static String latlng2String(LatLng latLng) {
        return String.format("Lon: %s\nLat: %s", latLng.getLongitude(), latLng.getLatitude());
    }

    public static String dataSetDesc2String(String desc) {
        String[] ss = desc.split(",");
        String size = ss[0], startTime = ss[1], endTime = ss[2];
        long st = Long.parseLong(startTime), et = Long.parseLong(endTime);
        return String.format("[%s], %s ~ %s",
                size, timestamp2LocalTime(st), timestamp2LocalTime(et));
    }

    public static String dataSetDesc2FileSuffix(String desc) {
        String[] ss = desc.split(",");
        String size = ss[0], startTime = ss[1], endTime = ss[2];
        long st = Long.parseLong(startTime), et = Long.parseLong(endTime);
        return String.format("%s_%s_%s",
                size, timestamp2ShortLocalTime(st), timestamp2ShortLocalTime(et));
    }

    public static <T> T checkNotNull(T reference) {
        if(reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }
}
