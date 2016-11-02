package ru.ionlabs.easymovies.util;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class DateUtil {

    @Nullable
    public static String formatDateString(String dateString) {
        String formatted = null;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString);
            formatted = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date);
        } catch (ParseException ex) {
            Timber.e(ex);
        }

        return formatted;
    }
}
