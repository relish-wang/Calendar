package wang.relish.datepicker;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Calendar;

public class Utils {

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param y 年份
     * @param m 月份
     * @return 这个月有多少天
     */
    public static int getMonthDays(int y, int m) {
        if (m < 1 || m > 12) throw new UnsupportedOperationException("根本没有这样的月！" + m + "月");
        return m == 2 ?
                ((y % 4 == 0 && y % 100 != 0 || y % 400 == 0) ? 29 : 28)
                : (m < 8 ^ m % 2 == 0 ? 31 : 30);
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据起始-结束时间计算期间月份数量
     *
     * @param mMinYear  起始年份
     * @param mMinMonth 起始年份对应月份
     * @param mMaxYear  结束年份
     * @param mMaxMonth 结束年份对应月份
     * @return 月份数量
     */
    public static int getMonthNum(int mMinYear, int mMinMonth, int mMaxYear, int mMaxMonth) {
        return (mMaxYear - mMinYear) * 12 + (mMaxMonth - mMinMonth) + 1;
    }

    /**
     * 根据两个日期（年、月、日）计算天数
     *
     * @param sy 开始年份
     * @param sm 开始月份
     * @param sd 开始日期
     * @param ey 结束年份
     * @param em 结束月份
     * @param ed 结束日期
     * @return 天数
     */
    public static int getDayNum(int sy, int sm, int sd, int ey, int em, int ed) {
        if (sy == ey) {
            return getDayNum(sy, sm, sd, em, ed);
        }
        int num = getDayNum(sy, sm, sd, 12, 31);
        for (int i = sy + 1; i < ey; i++) {
            num += getYearDays(i);
        }
        num += getDayNum(ey, 1, 1, em, ed);
        return num;
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
    private static int getDayNum(int year, int startMonth, int startDay, int endMonth, int endDay) {
        int num = 0;
        if (startMonth == endMonth) {
            num += endDay - startDay + 1;
            return num;
        } else {
            num += getMonthDays(year, startMonth) - startDay + 1;
            for (int i = startMonth + 1; i < endMonth; i++) {
                num += getMonthDays(year, i);
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

    public static boolean isDayCorrect(int mCurrYear, int mCurrMonth, int day) {
        return mCurrMonth >= 1 && mCurrMonth <= 12 && getMonthDays(mCurrYear, mCurrMonth) >= day && day >= 1;
    }

    /**
     * 当前入参日期是否是未来事件
     *
     * @param year  年
     * @param month 月(0~11)
     * @param day   日
     * @return 是否为未来
     */
    public static boolean isFuture(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        return year > y || year >= y && (month > m || month >= m && day > d);
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


    private Utils() {
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
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }
}
