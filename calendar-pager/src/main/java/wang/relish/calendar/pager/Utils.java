package wang.relish.calendar.pager;

import java.util.Calendar;

/**
 * @author Relish Wang
 * @since 2017/12/23
 */
public final class Utils extends wang.relish.calendar.Utils {


    private Utils() {
        super();
    }

    /**
     * @param calendar     判定日
     * @param year         当前MonthView所在年
     * @param month        当前MonthView所在月
     * @param weekFirstDay 设定的一周的第一天是周几
     * @return 是否属于这个MonthView
     */
    public static boolean isWithinMonthViewPage(Calendar calendar, @Stage int stage, int year, int month, int day, int weekFirstDay) {
        if (stage == Stage.OPEN) {
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            return y == year && month == m;
        } else if (stage == Stage.FOLD) {
            Calendar now = Calendar.getInstance();
            now.set(year, month, day, 0, 0, 0);
            int weekDayDay = now.get(Calendar.DAY_OF_WEEK);
            int dDay = weekDayDay - weekFirstDay;
            if (dDay < 0) dDay += 7;
            now.add(Calendar.DAY_OF_MONTH, -dDay);
            Calendar startDate = (Calendar) now.clone();
            now.add(Calendar.DAY_OF_MONTH, 6);
            Calendar endDate = (Calendar) now.clone();
            return isWithinDateSpan(startDate, endDate, calendar);
        } else {
            throw new IllegalArgumentException("no such stage: " + stage);
        }
    }


    /**
     * 手动滑动ViewPager时计算下一个选中的日期
     *
     * @param dir          方向(LEFT, RIGHT, NO_MOVEMENT[这个一般用不到])
     * @param stage        模式(OPEN, FOLD)
     * @param year         年
     * @param month        月
     * @param day          日
     * @param weekFirstDay 规定一周的第一天是周几
     * @return
     */
    public static Calendar getNextFocusDate(
            @Dir int dir,                       //左、右
            @Stage int stage,    //折叠、展开
            int year,                           //年
            int month,                          //月
            int day,                            //日
            int weekFirstDay) {
        if (!wang.relish.calendar.Utils.isDayCorrect(year, month, day)) {
            throw new IllegalArgumentException("no such date: " + year + "/" + month + "/" + day);
        }
        Calendar calendar = Calendar.getInstance();
        int y = year, m = month, d = day;
        if (stage == Stage.OPEN) {
            if (dir == Dir.LEFT) {
                y = m == 0 ? y - 1 : y;
                m = m == 0 ? 11 : m - 1;
            } else if (dir == Dir.RIGHT) {
                y = m == 11 ? y + 1 : y;
                m = m == 11 ? 0 : m + 1;
            } else {
                // never occur
            }
            d = 1;
        } else if (stage == Stage.FOLD) {
            int position = getPositionOfDateInMonthView(year, month, day, weekFirstDay);
            int linePreCount = position % 7;
            if (dir == Dir.LEFT) {
                if (position < 7) {
                    Calendar date = getPreMonthTailFirstDate(year, month, weekFirstDay);
                    date.add(Calendar.DAY_OF_MONTH, -7);
                    return date;
                } else if (position < 14) {
                    return getPreMonthTailFirstDate(year, month, weekFirstDay);
                } else {
                    d -= 7 + linePreCount;
                }
            } else if (dir == Dir.RIGHT) {
                int lastDayPosition = getMonthLastDayPosition(year, month, weekFirstDay);
                int line = position / 7;
                int lastLine = lastDayPosition / 7;
                if (line < lastLine) {
                    d += 7 - linePreCount;
                } else {
                    int monthDayCount = wang.relish.calendar.Utils.getMonthDayCount(y, m);
                    y = m == 11 ? y + 1 : y;
                    m = m == 11 ? 0 : m + 1;
                    d = day + (7 - linePreCount) - monthDayCount;
                }
            } else {
                // never occur
            }
        }
        calendar.set(y, m, d, 0, 0, 0);
        return calendar;
    }
}
