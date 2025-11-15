package com.efms.employee_file_ms_be.util;

import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
public final class DateUtils {

    public static LocalDate getStartDateOrDefault(LocalDate startDate) {
        return startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
    }

    public static LocalDate getEndDateOrDefault(LocalDate endDate) {
        return endDate != null ? endDate : LocalDate.now();
    }

    public static LocalDate[] resolveDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDate resolvedStart = getStartDateOrDefault(startDate);
        LocalDate resolvedEnd = getEndDateOrDefault(endDate);
        return new LocalDate[]{resolvedStart, resolvedEnd};
    }

    public static boolean shouldSearchIgnoringStatus(LocalDate start, LocalDate end) {
        LocalDate now = LocalDate.now();
        LocalDate currentMonthStart = now.withDayOfMonth(1);
        LocalDate lastMonthStart = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastMonthEnd = now.withDayOfMonth(1).minusDays(1);

        if (!end.isBefore(currentMonthStart)) {
            return true;
        }

        if (now.getDayOfMonth() <= 5) {
            return !start.isAfter(lastMonthEnd) && !end.isBefore(lastMonthStart);
        }

        return false;
    }

    public static LocalDate getStartDateFromPeriod(Integer period) {
        if (period == null) {
            return LocalDate.now().withDayOfMonth(1);
        }
        int year = period / 100;
        int month = period % 100;
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate getEndDateFromPeriod(Integer period) {
        if (period == null) {
            return LocalDate.now();
        }
        LocalDate startDate = getStartDateFromPeriod(period);
        return startDate.withDayOfMonth(startDate.lengthOfMonth());
    }
}
