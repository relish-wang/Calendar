package wang.relish.calendar.core;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/**
 * 月的样式
 *
 * @author Relish Wang
 * @since 2017/11/20
 */
public class MonthStyle implements Serializable {

    /**
     * 年份
     */
    private int year;
    /**
     * 月份
     */
    private int month;

    /**
     * 日(被选中的)
     */
    private int day;

    /**
     * 周的第一天（默认为周日）
     * 1~7: 周日~周六
     * 其实吧，设计稿上周的第一天是周一。我之所以这么做就是为了试试是否可以设置任何一天为周的第一天。
     *
     * @see Calendar#SUNDAY
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     */
    private int weekFirstDay = Calendar.MONDAY;

    /**
     * 日子们
     */
    private DateStyle[] dateCells;

    public MonthStyle(int year, int month, int day, int weekFirstDay) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.weekFirstDay = weekFirstDay;
    }


    public static MonthStyle createMonthStyle(
            int year,
            int month,
            int day,
            int weekFirstDay) {//Calendar.MONDAY
        return createMonthStyle(year, month, day, weekFirstDay, null);
    }

    public static MonthStyle createMonthStyle(
            int year,
            int month,
            int day,
            int weekFirstDay,//Calendar.MONDAY
            Map<String, Integer> dates) {
        boolean hasOutData = dates != null && dates.size() > 0;
        MonthStyle monthStyle = new MonthStyle(year, month, day, weekFirstDay);
        DateStyle[] dateStyles = new DateStyle[42];//默认6行
        int monthFirstDayDay = Utils.getMonthFirstDayDay(year, month); //日~六： 1~7
        int preDay = monthFirstDayDay - weekFirstDay;
        preDay = preDay < 0 ? preDay + 7 : preDay;

        int preMonthDayCount = Utils.getPreMonthDayCount(year, month);
        int j = 0;
        for (int i = preMonthDayCount - preDay + 1; i <= preMonthDayCount; i++, j++) {
            int y = month == 0 ? year - 1 : year;
            int m = month == 0 ? 11 : month - 1;
            boolean isToday = Utils.isToday(y, m, i);
            int badgeNumber = 0;
            if (hasOutData) {
                Integer integer = dates.get(String.format(Locale.ENGLISH, "%d%02d%02d", y, m + 1, i));
                if (integer != null) {
                    badgeNumber = integer;
                }
            }
            dateStyles[j] = DateStyle.createUnattainableStyle(i + "", badgeNumber, isToday);
        }
        int monthDayCount = Utils.getMonthDayCount(year, month);
        for (int i = 0; i < monthDayCount; i++, j++) {
            boolean isToday = Utils.isToday(year, month, i + 1);
            boolean isSelected = i + 1 == day;
            int badgeNumber = 0;
            if (hasOutData) {
                Integer integer = dates.get(String.format(Locale.ENGLISH, "%d%02d%02d",
                        year, month + 1, i + 1));
                if (integer != null) {
                    badgeNumber = integer;
                }
            }
            dateStyles[j] = DateStyle.createNormalStyle(
                    isToday ? "今天" : String.valueOf(i + 1), //"今天" 或 "27"
                    badgeNumber, // 数字角标
                    isSelected, //是否选中
                    isToday); //是否今天(样式相关)
        }
        for (int i = 1; j < dateStyles.length; j++, i++) {
            int y = month == 11 ? year + 1 : year;
            int m = month == 11 ? 0 : month + 1;
            boolean isToday = Utils.isToday(y, m, i);
            int badgeNumber = 0;
            if (hasOutData) {
                Integer integer = dates.get(String.format(Locale.ENGLISH, "%d%02d%02d",
                        y, m + 1, i));
                if (integer != null) {
                    badgeNumber = integer;
                }
            }
            dateStyles[j] = DateStyle.createUnattainableStyle(
                    isToday ? "今天" : i + "",
                    badgeNumber,
                    isToday);

        }
        monthStyle.setDateCells(dateStyles);
        monthStyle.setSelectedDay(day);
        return monthStyle;

    }

    public static MonthStyle getMonthStyleDemo() {
        int weekFirstDay = Calendar.MONDAY;//每周的第一天是周？
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = 1;
        int monthDayCount = Utils.getMonthDayCount(year, month);
        DateStyle[] dateStyles = new DateStyle[42];//默认6行
        int monthFirstDayDay = Utils.getMonthFirstDayDay(year, month); //日~六： 1~7
        int preDay = monthFirstDayDay - weekFirstDay;
        preDay = preDay < 0 ? preDay + 7 : preDay;

        int preMonthDayCount = Utils.getPreMonthDayCount(year, month);
        int j = 0;
        for (int i = preMonthDayCount - preDay + 1; i <= preMonthDayCount && j < dateStyles.length; i++, j++) {
            dateStyles[j] = DateStyle.createUnattainableStyle(i + "", 0, false);
        }
        for (int i = 0; i < monthDayCount && j < dateStyles.length; i++, j++) {
            if (i == 0) {
                dateStyles[j] = DateStyle.createNormalStyle((i + 1) + "", 86, true, true);
            } else if (i < 5) {
                dateStyles[j] = DateStyle.createNormalStyle((i + 1) + "", 1, false, false);
            } else {
                dateStyles[j] = DateStyle.createNormalStyle((i + 1) + "", 0, false, false);
            }
        }
        for (int i = 1; j < dateStyles.length; j++, i++) {
            dateStyles[j] = DateStyle.createUnattainableStyle(i + "", 0, false);
        }
        MonthStyle monthStyle = new MonthStyle(year, month, day, Calendar.MONDAY);
        monthStyle.setWeekFirstDay(weekFirstDay);
        monthStyle.setDateCells(dateStyles);
        return monthStyle;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getWeekFirstDay() {
        return weekFirstDay;
    }

    public void setWeekFirstDay(int weekFirstDay) {
        this.weekFirstDay = weekFirstDay;
    }

    public DateStyle[] getDateCells() {
        return dateCells;
    }

    /**
     * 只有本月的日子可以这么设置
     *
     * @param day
     */
    public void setSelectedDay(int day) {
        int monthDayCount = Utils.getMonthDayCount(year, month);
        if (day < 1 || day > monthDayCount) {
            Log.e("CalendarView", "no such day: " + year + "年" + month + "月" + day + "日");
            return;
        }
        int monthFirstDayDay = Utils.getMonthFirstDayDay(year, month); //日~六： 1~7
        int preDay = monthFirstDayDay - weekFirstDay;
        preDay = preDay < 0 ? preDay + 7 : preDay;
        int oldIndex = preDay + this.day - 1;
        int index = preDay + day - 1;

        dateCells[oldIndex].setSelected(false);
        if (Utils.isToday(year, month, day)) {
            dateCells[oldIndex].setTextColor(DateStyle.ACTIVE_TEXT_COLOR);
        }
        dateCells[index].setSelected(true);
        this.day = day;
    }

    public DateStyle[] getDateStyle() {
        return dateCells;
    }

    public void setDateCells(DateStyle[] dateCells) {
        this.dateCells = dateCells;
    }
}
