package com.mirrordust.telecomlocate.util;

import com.mirrordust.telecomlocate.entity.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiaoShanhe on 2017/07/25/025.
 */

public class Converter {
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
    public static String Latlng2String(LatLng latLng) {
        return String.format("Lon: %s\nLat: %s", latLng.getLongitude(), latLng.getLatitude());
    }

}
