/*
 * Copyright 2014 jmrozanec
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cronutils.utils;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class DateUtils {
    private DateUtils(){}

    public static int workdaysCount(final ZonedDateTime startDate, final int days, final List<ZonedDateTime> holidays, final WeekendPolicy weekendPolicy) {
        return workdaysCount(startDate, startDate.plusDays(days), holidays, weekendPolicy);
    }

    public static int workdaysCount(final ZonedDateTime startDate, final ZonedDateTime endDate, List<ZonedDateTime> holidays, final WeekendPolicy weekendPolicy) {
        Collections.sort(holidays);
        holidays = holidaysInRange(startDate, endDate, holidays);
        final int daysToWeekend = WeekendPolicy.daysToWeekend(weekendPolicy, startDate);
        final int daysFromWeekend = WeekendPolicy.daysFromWeekend(weekendPolicy, endDate);
        final int daysBetween = (int)Duration.between(startDate, endDate).toDays() + 1;

        //2+ [xxx+2]/7*5
        int tmpWeekdays = (daysBetween - daysToWeekend - daysFromWeekend - 2) / 7 * 5;
        tmpWeekdays = tmpWeekdays - holidays.size();

        return tmpWeekdays + daysToWeekend + daysFromWeekend;
    }

    private static List<ZonedDateTime> holidaysInRange(final ZonedDateTime startDate, final ZonedDateTime endDate, final List<ZonedDateTime> holidays) {
        if (holidays.isEmpty()) {
            return holidays;
        }
        final int idxstart = findIdx(0, holidays.size() - 1, startDate, holidays);
        final int idxend = findIdx(0, holidays.size() - 1, endDate, holidays);
        return holidays.subList(idxstart, idxend + 1);
    }

    private static int findIdx(final int startidx, final int endidx, final ZonedDateTime endDate, final List<ZonedDateTime> holidays) {
        if (startidx == endidx) {
            return startidx;
        }
        int pivot = (endidx - startidx) / 2;

        if (holidays.get(pivot).equals(endDate)) {
            return pivot;
        }
        if (holidays.get(pivot).isBefore(endDate)) {
            return findIdx(++pivot, endidx, endDate, holidays);
        } else {
            return findIdx(startidx, pivot, endDate, holidays);
        }
    }
}
