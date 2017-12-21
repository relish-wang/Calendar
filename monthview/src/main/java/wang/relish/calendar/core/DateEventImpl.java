package wang.relish.calendar.core;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Relish Wang
 * @since 2017/12/11
 */
public class DateEventImpl implements DateEvent {

    private String date;
    private int eventCount;


    public DateEventImpl(String date, int eventCount) {
        setDate(date);
        setEventCount(eventCount);
    }

    /**
     * month自动转化为[0,11]
     *
     * @param date eg:2017-12-02
     */
    public void setDate(String date) {
        if (TextUtils.isEmpty(date)) return;
        Matcher matcher = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$").matcher(date);
        if (!matcher.matches()) throw new IllegalArgumentException("no such date: " + date);
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));
        if (!Utils.isDayCorrect(year, month - 1, day)) {
            throw new IllegalArgumentException("no such date: " + date);
        }
        this.date = date.replace("-", "");
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    @Override
    public String getDate() {
        if (TextUtils.isEmpty(date)) throw new IllegalArgumentException("Did you forget init?");
        return date;
    }

    @Override
    public int getEventCount() {
        return eventCount;
    }
}
