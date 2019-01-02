package wang.relish.calendar;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.util.Calendar;

import static wang.relish.calendar.DateStyle.NORMAL_TEXT_COLOR;
import static wang.relish.calendar.DateStyle.UNATTAINABLE_TEXT_COLOR;

/**
 * @author Relish Wang
 * @since 2017/3/16
 */
public final class Utils {

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param y 年份
     * @param m 月份[0,11]
     * @return 这个月有多少天
     */
    public static int getMonthDayCount(int y, int m) {
        if (m < 0 || m > 11) throw new UnsupportedOperationException("根本没有这样的月！" + (m + 1) + "月");
        return m == 1 ?
                ((y % 4 == 0 && y % 100 != 0 || y % 400 == 0) ? 29 : 28)
                : (m < 7 ^ m % 2 == 1 ? 31 : 30);
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份[0,11]
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getMonthFirstDayDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 返回当前MonthView有多少前一个月的日子
     *
     * @param year  年份
     * @param month 月份[0,11]
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int preMonthTailDayCount(int year, int month, int weekFirstDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int preDay = calendar.get(Calendar.DAY_OF_WEEK) - weekFirstDay;
        return preDay < 0 ? preDay + 7 : preDay;
    }


    /**
     * 返回当前MonthView最后一天在第几行第几列
     *
     * @param year  年份
     * @param month 月份[0,11]
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static Point getMonthLastDayPoint(int year, int month, int weekFirstDay) {
        int monthLastDayPosition = getMonthLastDayPosition(year, month, weekFirstDay);
        int row = (monthLastDayPosition) / 7;
        int col = (monthLastDayPosition) % 7;
        return new Point(col, row);
    }

    public static int getMonthLastDayPosition(int year, int month, int weekFirstDay) {
        int preDayCount = preMonthTailDayCount(year, month, weekFirstDay);
        int monthDayCount = getMonthDayCount(year, month);
        return preDayCount + monthDayCount;
    }


    /**
     * 同一年内，根据月日计算天数
     *
     * @param year       年
     * @param startMonth 开始月份
     * @param startDay   开始日期
     * @param endMonth   结束月份
     * @param endDay     结束日期
     * @return 天数
     */
    private static int getDayCount(int year, int startMonth, int startDay, int endMonth, int endDay) {
        int num = 0;
        if (startMonth == endMonth) {
            num += endDay - startDay + 1;
            return num;
        } else {
            num += getMonthDayCount(year, startMonth) - startDay + 1;
            for (int i = startMonth + 1; i < endMonth; i++) {
                num += getMonthDayCount(year, i);
            }
            num += endDay;
            return num;
        }
    }

    /**
     * 根据年份获得该年有多少天
     *
     * @param year 年份
     * @return 365 | 366
     */
    private static int getYearDays(int year) {
        return 365 + ((year % 400 == 0 || year % 4 == 0 && year % 100 != 0) ? 1 : 0);
    }

    /**
     * @param year  年(eg: 2017)
     * @param month 月[0,11]
     * @param day   日[1,31]
     * @return
     */
    public static boolean isDayCorrect(int year, int month, int day) {
        return month >= 0 && month <= 11 && getMonthDayCount(year, month) >= day && day >= 1;
    }

    /**
     * 判断一个日期是否在设定的日期范围内（边界值包含在范围内）
     *
     * @param minDate  范围日期下限
     * @param maxDate  范围日期上线
     * @param currDate 当前日期
     * @return 是否在范围内
     */
    public static boolean isWithinDateSpan(Calendar minDate, Calendar maxDate, Calendar currDate) {
        long min = minDate.getTimeInMillis(), max = maxDate.getTimeInMillis(), curr = currDate.getTimeInMillis();
        return curr >= min && curr <= max;
    }

    /**
     * 判断一个日期是否在设定的日期范围内（边界值包含在范围内）
     *
     * @param minYear   下限年份
     * @param minMonth  下限月份
     * @param minDay    下限日期
     * @param maxYear   上限年份
     * @param maxMonth  上限月份
     * @param maxDay    上限日期
     * @param currYear  当前年份
     * @param currMonth 当前月份
     * @param currDay   当前日期
     * @return 是否在范围内
     */
    public static boolean isWithinDateSpan(int minYear, int minMonth, int minDay, int maxYear, int maxMonth, int maxDay, int currYear, int currMonth, int currDay) {
        Calendar minCalendar = Calendar.getInstance();
        Calendar maxCalendar = Calendar.getInstance();
        Calendar currCalendar = Calendar.getInstance();
        minCalendar.clear();
        maxCalendar.clear();
        currCalendar.clear();
        minCalendar.set(minYear, minMonth - 1, minDay);
        maxCalendar.set(maxYear, maxMonth - 1, maxDay);
        currCalendar.set(currYear, currMonth - 1, currDay);
        return isWithinDateSpan(minCalendar, maxCalendar, currCalendar);
    }


    protected Utils() {
        throw new UnsupportedOperationException("这个类不能被实例化~biu~biu~");
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        if (windowManager == null) return 0;
        Display defaultDisplay = windowManager.getDefaultDisplay();
        if (defaultDisplay == null) return 0;
        defaultDisplay.getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }


    /**
     * Converts an unpacked complex data value holding a dimension to its final floating
     * point value. The two parameters <var>unit</var> and <var>value</var>
     * are as in {@link TypedValue#TYPE_DIMENSION}.
     *
     * @param unit  The unit to convert from.
     * @param value The value to apply the unit to.
     * @return The complex floating point value multiplied by the appropriate
     * metrics depending on its unit.
     */
    public static int applyDimension(Context context, int unit, float value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float result = value;
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                result = value;
                break;
            case TypedValue.COMPLEX_UNIT_DIP:
                result = value * metrics.density;
                break;
            case TypedValue.COMPLEX_UNIT_SP:
                result = value * metrics.scaledDensity;
                break;
            case TypedValue.COMPLEX_UNIT_PT:
                result = value * metrics.xdpi * (1.0f / 72);
                break;
            case TypedValue.COMPLEX_UNIT_IN:
                result = value * metrics.xdpi;
                break;
            case TypedValue.COMPLEX_UNIT_MM:
                result = value * metrics.xdpi * (1.0f / 25.4f);
                break;
        }
        return (int) ((result >= 0) ? (result + 0.5f) : (result - 0.5f));
    }

    /**
     * 获取上个月的总天数
     *
     * @param year  当前年
     * @param month 当前月[0,11]
     * @return 总天数
     */
    public static int getPreMonthDayCount(int year, int month) {
        if (month == 0) {
            year--;
            month = 11;
        } else {
            month--;
        }
        return getMonthDayCount(year, month);
    }

    public static boolean isToday(int year, int month, int day) {
        if (!Utils.isDayCorrect(year, month, day)) {
            throw new IllegalArgumentException("no such date: " + year + "/" + month + "/" + day);
        }
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        if (y != year) return false;
        int m = calendar.get(Calendar.MONTH);
        if (m != month) return false;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        return d == day;
    }

    /**
     * @param calendar     判定日
     * @param year         当前MonthView所在年
     * @param month        当前MonthView所在月
     * @param weekFirstDay 设定的一周的第一天是周几
     * @return 是否属于这个MonthView
     */
    public static boolean isWithinMonthView(Calendar calendar, int year, int month, int weekFirstDay) {
        Calendar startDate = getPreMonthTailFirstDate(year, month, weekFirstDay);
        Calendar endDate = getNextMonthHeadLastDate(year, month, weekFirstDay);
        return isWithinDateSpan(startDate, endDate, calendar);
    }

    /**
     * 获取前一个月的最后一行的星期行的第一天的日期
     *
     * @param year         MonthView显示的当前年
     * @param month        MonthView显示的当前月
     * @param weekFirstDay 设定的一周的第一天是周几
     * @return 如2017, 11, 星期一。返回2017年10月30日(建议看看日历,更容易理解这个方法)
     */
    public static Calendar getPreMonthTailFirstDate(int year, int month, int weekFirstDay) {
        if (!Utils.isDayCorrect(year, month, 1)) {
            throw new IllegalArgumentException("no such date: " + year + "/" + month + "/" + 1);
        }
        int monthFirstDayDay = Utils.getMonthFirstDayDay(year, month); //日~六： 1~7
        int preDay = monthFirstDayDay - weekFirstDay;
        if (preDay < 0) preDay += 7;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -preDay);
        return calendar;
    }

    /**
     * 获取下一个月的第一行的星期行的第一天的日期
     *
     * @param year         MonthView显示的当前年
     * @param month        MonthView显示的当前月
     * @param weekFirstDay 设定的一周的第一天是周几
     * @return 如2017, 11, 星期一。返回2017年11月27日(建议看看日历,更容易理解这个方法)
     */
    private static Calendar getNextMonthHeadLastDate(int year, int month, int weekFirstDay) {
        if (!Utils.isDayCorrect(year, month, 1)) {
            throw new IllegalArgumentException("no such date: " + year + "/" + month + "/" + 1);
        }
        int monthFirstDayDay = Utils.getMonthFirstDayDay(year, month); //日~六： 1~7
        int preDay = monthFirstDayDay - weekFirstDay;
        int count = preDay + Utils.getMonthDayCount(year, month);
        int day = 42 - count;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        calendar.add(Calendar.MONTH, 1);
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        calendar.set(y, m, day, 0, 0, 0);
        return calendar;
    }

    /**
     * 样例数据
     */
    static MonthStyle getMonthStyleDemo() {
        int weekFirstDay = Calendar.MONDAY;//每周的第一天是周？
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int monthDayCount = Utils.getMonthDayCount(year, month);
        DateStyle[] dateStyles = new DateStyle[42];//默认6行
        int monthFirstDayDay = Utils.getMonthFirstDayDay(year, month); //日~六： 1~7
        int preDay = monthFirstDayDay - weekFirstDay;
        preDay = preDay < 0 ? preDay + 7 : preDay;

        int preMonthDayCount = Utils.getPreMonthDayCount(year, month);
        int j = 0;
        for (int i = preMonthDayCount - preDay + 1; i <= preMonthDayCount && j < dateStyles.length; i++, j++) {
            dateStyles[j] = new DateStyle(i + "", UNATTAINABLE_TEXT_COLOR);
        }
        for (int i = 0; i < monthDayCount && j < dateStyles.length; i++, j++) {
            dateStyles[j] = new DateStyle((i + 1) + "", NORMAL_TEXT_COLOR);
        }
        for (int i = 1; j < dateStyles.length; j++, i++) {
            dateStyles[j] = new DateStyle(i + "", UNATTAINABLE_TEXT_COLOR);
        }
        MonthStyle monthStyle = new MonthStyle(year, month, Calendar.MONDAY);
        monthStyle.setWeekFirstDay(weekFirstDay);
        monthStyle.setDateCells(dateStyles);
        return monthStyle;
    }

    /**
     * 获得这个月需显示几行
     *
     * @param year         当前日
     * @param month        当前月
     * @param weekFirstDay 一周的第一天是周几
     * @return 4 | 5| 6
     */
    public static int getMonthRowCount(int year, int month, int weekFirstDay) {
        int monthDayCount = getMonthDayCount(year, month);
        int preMonthTailDayCount = preMonthTailDayCount(year, month, weekFirstDay);
        boolean isDivideExactly = (monthDayCount + preMonthTailDayCount) % 7 == 0;
        int row = (monthDayCount + preMonthTailDayCount) / 7;
        return isDivideExactly ? row : row + 1;
    }


    private static Point mLatestClickedPoint;
    private static long mLatestClickedTime = -1;

    /**
     * 判断是否异常的双击（300毫秒内不能点击不同控件，500毫秒内不能点击相同控件）
     */
    public static boolean isFastDoubleClick(@NonNull Point point) {
        final long now = System.currentTimeMillis();
        if (mLatestClickedPoint == null) {
            mLatestClickedPoint = point;
            mLatestClickedTime = now;
            return false;
        }
        final long interval = now - mLatestClickedTime;
        if (mLatestClickedPoint.equals(point) && interval < 300) {
            mLatestClickedTime = now;
            return true;
        }
        if (!mLatestClickedPoint.equals(point) && interval < 500) {
            mLatestClickedPoint = point;
            mLatestClickedTime = now;
            return true;
        }
        return false;
    }

//    /**
//     * 根据起始-结束时间计算期间月份数量
//     *
//     * @param mMinYear  起始年份
//     * @param mMinMonth 起始年份对应月份
//     * @param mMaxYear  结束年份
//     * @param mMaxMonth 结束年份对应月份
//     * @return 月份数量
//     */
//    public static int getMonthCount(int mMinYear, int mMinMonth, int mMaxYear, int mMaxMonth) {
//        return (mMaxYear - mMinYear) * 12 + (mMaxMonth - mMinMonth) + 1;
//    }

//    /**
//     * 根据两个日期（年、月、日）计算天数
//     *
//     * @param sy 开始年份
//     * @param sm 开始月份
//     * @param sd 开始日期
//     * @param ey 结束年份
//     * @param em 结束月份
//     * @param ed 结束日期
//     * @return 天数
//     */
//    public static int getDayCount(int sy, int sm, int sd, int ey, int em, int ed) {
//        if (sy == ey) {
//            return getDayCount(sy, sm, sd, em, ed);
//        }
//        int num = getDayCount(sy, sm, sd, 12, 31);
//        for (int i = sy + 1; i < ey; i++) {
//            num += getYearDays(i);
//        }
//        num += getDayCount(ey, 1, 1, em, ed);
//        return num;
//    }

//    public static Point getPointOfDateInMonthView(int year, int month, int day, int weekFirstDay) {
//        if (!Utils.isDayCorrect(year, month, day)) {
//            throw new IllegalArgumentException("no such date: " + year + "/" + month + "/" + day);
//        }
//        int pos = getPositionOfDateInMonthView(year, month, day, weekFirstDay);
//        return new Point(pos % 7, pos / 7);
//    }
//
//    /**
//     * 当前日期是不是与上个月月尾共享一行
//     */
//    public static boolean isWithinPreMonth(int year, int month, int day, int weekFirstDay) {
//        if (!Utils.isDayCorrect(year, month, day)) {
//            throw new IllegalArgumentException("no such date: " + year + "/" + month + "/" + day);
//        }
//        int position = getPositionOfDateInMonthView(year, month, day, weekFirstDay);
//        return position < 7;
//    }
//
//    /**
//     * 当前日期是不是与下个月月初共享一行
//     */
//    public static boolean isWithinNextMonth(int year, int month, int day, int weekFirstDay) {
//        if (!Utils.isDayCorrect(year, month, day)) {
//            throw new IllegalArgumentException("no such date: " + year + "/" + month + "/" + day);
//        }
//        int position = getPositionOfDateInMonthView(year, month, day, weekFirstDay);
//        int monthLastDayPosition = getMonthLastDayPosition(year, month, weekFirstDay);
//        return (position / 7) == (monthLastDayPosition / 7);
//    }
//
//
//    /**
//     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
//     */
//    public static int px2dip(Context context, float pxValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (pxValue / scale + 0.5f);
//    }
//
}
