package org.prikic.yafr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Util {

    public static String parseDate(String pubDate) {

        List<SimpleDateFormat> knownPatterns = new ArrayList<>();
        knownPatterns.add(new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.US));
        knownPatterns.add(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US));

        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                // Take a try
                Date date = pattern.parse(pubDate);
                SimpleDateFormat dt1 = new SimpleDateFormat("dd MM yyyy - HH:mm", Locale.US);
                dt1.setTimeZone(TimeZone.getTimeZone("UTC"));
                return dt1.format(date);

            } catch (ParseException pe) {
                // Loop on
            }
        }
        return "";

    }

    public static String parseDateToISO8601(String pubDate) {

        List<SimpleDateFormat> knownPatterns = new ArrayList<>();
        knownPatterns.add(new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.US));
        knownPatterns.add(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US));

        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                // Take a try
                Date date = pattern.parse(pubDate);

                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
                dt1.setTimeZone(TimeZone.getTimeZone("UTC"));
                return dt1.format(date);

            } catch (ParseException pe) {
                // Loop on
            }
        }
        return null;

    }
}
