package wang.relish.datepicker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import wang.relish.calendar.pager.CalendarView;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CalendarView(this));
    }
}
