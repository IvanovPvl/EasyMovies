package ru.ionlabs.easymovies.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DateUtilTest {

    @Test
    public void dateIsCorrect() {
        assertEquals("June 17, 1994", DateUtil.formatDateString("1994-06-17"));
    }

    @Test
    public void dateIsNull() {
        assertNull(DateUtil.formatDateString(""));
    }
}
