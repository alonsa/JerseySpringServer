package com.alon.main.server.util;

import java.util.concurrent.TimeUnit;

/**
 * Created by alon_ss on 7/30/16.
 */
public class Util {

    public static String millisecondDurationToDate(Long millis){
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
