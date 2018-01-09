package wang.relish.calendar;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

/**
 * 月的样式
 *
 * @author Relish Wang
 * @since 2017/11/20
 */
public class MonthStyle<T extends DateStyle> extends Attributes implements Serializable {

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
    private T[] dateCells;

    private Map<String, Object> ext;

    public MonthStyle(int year, int month, int day, int weekFirstDay) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.weekFirstDay = weekFirstDay;
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

    public T[] getDateCells() {
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

    public void setDateCells(T[] dateCells) {
        this.dateCells = dateCells;
    }

}
