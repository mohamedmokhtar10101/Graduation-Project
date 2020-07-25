package com.gp.findlost.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {
//    public static List<BookingDate> getNextXDays(int days) {
//        List<BookingDate> bookingDates = new ArrayList<>();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE");
//        SimpleDateFormat dayNumberFormat = new SimpleDateFormat("d");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//
//        for (int i = 0; i < days; i++) {
//            bookingDates.add(new BookingDate(dayNameFormat.format(calendar.getTime()),
//                    Integer.parseInt(dayNumberFormat.format(calendar.getTime())),
//                    dateFormat.format(calendar.getTime()), calendar.get(Calendar.DAY_OF_WEEK)));
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        return bookingDates;
//    }

    public static String get12HTime(String time) {
        if (Integer.parseInt(time.substring(0, 2)) == 12) {
            return "12:" + time.substring(3) + " PM";
        } else if (Integer.parseInt(time.substring(0, 2)) == 0 || Integer.parseInt(time.substring(0, 2)) == 24) {
            return "12:" + time.substring(3) + " AM";
        } else {
            String convertedTime = "";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                Date dateObj = sdf.parse(time);
                convertedTime = new SimpleDateFormat("KK:mm a", Locale.ENGLISH).format(dateObj);
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            return convertedTime;
        }
    }
}
