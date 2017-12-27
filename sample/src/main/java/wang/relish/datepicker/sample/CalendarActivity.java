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
        dateEvents.put("20171226", 99);
        dateEvents.put("20171227", 99);
        dateEvents.put("20171228", 99);
        dateEvents.put("20171229", 99);
        dateEvents.put("20171230", 99);
        dateEvents.put("20171231", 99);
        dateEvents.put("20180101", 99);

        CalendarView view = new CalendarView(this);
        view.setWeekFirstDay(Calendar.THURSDAY);
        view.setData(dateEvents);
        setContentView(view);
    }
}
