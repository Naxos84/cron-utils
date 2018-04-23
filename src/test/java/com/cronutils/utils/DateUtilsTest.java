package com.cronutils.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void workdaysCountPolicyThursday() {
        final LocalDate date = LocalDate.of(2018, 1, 6);//this is a saturday
        final int daysToEndDate = 1;//sunday
        final WeekendPolicy policy = WeekendPolicy.THURSDAY_FRIDAY;
        final int workdaysCount = DateUtils.workdaysCount(ZonedDateTime.of(date, LocalTime.of(1, 0), ZoneId.of("America/Argentina/Buenos_Aires")), daysToEndDate, new ArrayList<>(), policy);
        assertEquals(2, workdaysCount);
    }

    @Test
    public void workdaysCountPolicyFriday() {
        final LocalDate date = LocalDate.of(2018, 1, 6);//this is a saturday
        final int daysToEndDate = 1;//sunday
        final WeekendPolicy policy = WeekendPolicy.FRIDAY_SATURDAY;
        final int daysToWorkday = DateUtils.workdaysCount(ZonedDateTime.of(date, LocalTime.of(1, 0), ZoneId.of("America/Argentina/Buenos_Aires")), daysToEndDate, new ArrayList<>(), policy);
        assertEquals(1, daysToWorkday);
    }

    @Test
    public void workdaysCountPolicySaturday() {
        final LocalDate date = LocalDate.of(2018, 1, 6);//this is a saturday
        final int daysToEndDate = 1;//sunday
        final WeekendPolicy policy = WeekendPolicy.SATURDAY_SUNDAY;
        final int daysToWorkday = DateUtils.workdaysCount(ZonedDateTime.of(date, LocalTime.of(1, 0), ZoneId.of("America/Argentina/Buenos_Aires")), daysToEndDate, new ArrayList<>(), policy);
        assertEquals(0, daysToWorkday);
    }

    @Test
    public void workdaysMay2018Argentina() {
        final ZonedDateTime start = ZonedDateTime.of(LocalDate.of(2018, 5, 1), LocalTime.of(1, 0), ZoneId.of("America/Argentina/Buenos_Aires"));
        final ZonedDateTime end = ZonedDateTime.of(LocalDate.of(2018, 5, 31), LocalTime.of(1, 0), ZoneId.of("America/Argentina/Buenos_Aires"));
        final List<ZonedDateTime> holidays = Arrays.asList(
                ZonedDateTime.of(LocalDate.of(2018, 5, 1), LocalTime.of(1, 0), ZoneId.of("America/Argentina/Buenos_Aires")),
                ZonedDateTime.of(LocalDate.of(2018, 5, 25), LocalTime.of(1, 0), ZoneId.of("America/Argentina/Buenos_Aires"))
        );
        final WeekendPolicy policy = WeekendPolicy.SATURDAY_SUNDAY;
        final int daysToWorkday = DateUtils.workdaysCount(start, end, holidays, policy);
        assertEquals(21, daysToWorkday);
    }
}