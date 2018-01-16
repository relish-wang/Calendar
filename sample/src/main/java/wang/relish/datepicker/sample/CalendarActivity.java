package wang.relish.datepicker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import wang.relish.calendar.pager.CalendarView;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, Integer> dateEvents = new HashMap<>();
        for (int i = 16; i < 31; i++) {
            dateEvents.put("201801" + i, 999);
        }

        CalendarView view = new CalendarView(this);
        view.setWeekFirstDay(Calendar.THURSDAY);
        view.setData(dateEvents);
        setContentView(view);
    }
}
