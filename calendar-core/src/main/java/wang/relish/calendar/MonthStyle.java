package wang.relish.calendar;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

/**
 * 月的样式
 *
 * @author Relish Wang
 * @since 2017/11/20
 */
public class MonthStyle extends Attributes implements Serializable {

    /**
     * 年份
     */
    private int year;
    /**
     * 月份
     */
    private int month;

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

    public MonthStyle(int year, int month, int weekFirstDay) {
        this.year = year;
        this.month = month;
        this.weekFirstDay = weekFirstDay;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
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

    public DateStyle[] getDateStyle() {
        return dateCells;
    }

    public void setDateCells(DateStyle[] dateCells) {
        this.dateCells = dateCells;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"").append("year").append("\":\"").append(year).append("\",");
        sb.append("\"").append("month").append("\":\"").append(month).append("\",");
        sb.append("\"").append("weekFirstDay").append("\":\"").append(weekFirstDay).append("\",");
        sb.append("\"").append("dateCells").append("\":").append(Arrays.toString(dateCells)).append(",");
        sb.append("\"").append("ext").append("\":\"").append(super.toString()).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
