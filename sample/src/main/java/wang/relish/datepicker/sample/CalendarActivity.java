package wang.relish.datepicker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import wang.relish.calendar.Utils;
import wang.relish.calendar.pager.CalendarView;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM", Locale.ENGLISH);
        Date date = new Date();
        int y = date.getYear();
        int m = date.getMonth();
        String dateStr = format.format(date);
        Map<String, Integer> dateEvents = new HashMap<>();
        for (int i = 1; i <= Utils.getMonthDayCount(y, m); i++) {
            dateEvents.put(dateStr + String.format(Locale.ENGLISH, "%02d", i), 999);
        }

        CalendarView view = new CalendarView(this);
        view.setWeekFirstDay(Calendar.SUNDAY);
        view.setData(dateEvents);
        setContentView(view);
    }
}
