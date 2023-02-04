package com.elad.chatimeapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author - Elad Sabag
 * @date - 1/21/2023
 */
public class CalendarUtils {
    /**
     * This function gets the current date and time.
     * @return the current date and time in milliseconds.
     */
    public static long getCurrentTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * This function gets the current date in the format yyyy-MM-dd
     * @return the current date as a string
     */
    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * This function gets the current time in the format HH:mm:ss
     * @return the current time as a string
     */
    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * This function gets the date of the next Monday
     * @return the date of the next Monday as a string in the format yyyy-MM-dd
     */
    public static String getNextMonday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * This function gets the date of the next Sunday
     * @return the date of the next Sunday as a string in the format yyyy-MM-dd
     */
    public static String getNextSunday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * This function get the number of days in a specific month and year.
     * @param year - the year of the month.
     * @param month - the month in the year.
     * @return the number of days in the month.
     */
    public static int getNumberOfDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * This function get the name of the day of the week of a specific date.
     * @param year - the year of the date.
     * @param month - the month of the date.
     * @param day - the day of the date.
     * @return the name of the day of the week.
     */
    public static String getDayOfWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * This function calculate the number of days between two dates
     * @param startYear - the year of the start date.
     * @param startMonth - the month of the start date.
     * @param startDay - the day of the start date.
     * @param endYear - the year of the end date.
     * @param endMonth - the month of the end date.
     * @param endDay - the day of the end date.
     * @return the number of days between the two dates.
     */
    public static long getNumberOfDaysBetweenDates(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startMonth, startDay);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDay);
        long startTime = startCalendar.getTimeInMillis();
        long endTime = endCalendar.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(endTime - startTime));
    }

    /**
     * This function get the time difference between two times in the format of hh:mm:ss
     * @param startTime - the start time in the format of hh:mm:ss
     * @param endTime - the end time in the format of hh:mm:ss
     * @return the time difference in the format of hh:mm:ss
     */
    public static String getTimeDifference(String startTime, String endTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(startTime);
        Date date2 = format.parse(endTime);
        long diff = date2.getTime() - date1.getTime();
        return format.format(new Date(diff));
    }

    /**
     * This function calculate the number of weeks between two dates
     * @param startYear - the year of the start date.
     * @param startMonth - the month of the start date.
     * @param startDay - the day of the start date.
     * @param endYear - the year of the end date.
     * @param endMonth - the month of the end date.
     * @param endDay - the day of the end date.
     * @return the number of weeks between the two dates.
     */
    public static long getNumberOfWeeksBetweenDates(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startMonth, startDay);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDay);
        long startTime = startCalendar.getTimeInMillis();
        long endTime = endCalendar.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(endTime - startTime)) / 7;
    }

    /**
     * This function calculate the number of months between two dates
     * @param startYear - the year of the start date.
     * @param startMonth - the month of the start date.
     * @param startDay - the day of the start date.
     * @param endYear - the year of the end date.
     * @param endMonth - the month of the end date.
     * @param endDay - the day of the end date.
     * @return the number of months between the two dates.
     */
    public static int getNumberOfMonthsBetweenDates(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startMonth, startDay);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDay);
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return diffMonth;
    }

    /**
     * This function calculate the number of years between two dates
     * @param startYear - the year of the start date.
     * @param startMonth - the month of the start date.
     * @param startDay - the day of the start date.
     * @param endYear - the year of the end date.
     * @param endMonth - the month of the end date.
     * @param endDay - the day of the end date.
     * @return the number of years between the two dates.
     */
    public static int getNumberOfYearsBetweenDates(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startMonth, startDay);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDay);
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        return diffYear;
    }

    /**
     * This function get the date of a specific day of the week in a specific week
     * @param year - the year of the date.
     * @param month - the month of the date.
     * @param week - the week of the date.
     * @param dayOfWeek - the day of the week of the date.
     * @return the date of the specific day of the week in the specific week in the format yyyy-MM-dd
     */
    public static String getDateOfWeek(int year, int month, int week, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.WEEK_OF_MONTH, week);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * This function get the number of days in a specific year.
     * @param year - the year of the date.
     * @return the number of days in the specific year
     */
    public static int getNumberOfDaysInYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    /**
     * This function get the day of the year for a specific date.
     * @param year - the year of the date.
     * @param month - the month of the date.
     * @param day - the day of the date.
     * @return the day of the year
     */
    public static int getDayOfYear(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
}
