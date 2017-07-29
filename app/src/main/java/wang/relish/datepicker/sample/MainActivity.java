package wang.relish.datepicker.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import wang.relish.datepicker.DatePickerDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv_picker);
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        DatePickerDialog dialog = DatePickerDialog.newInstance();
        if (!(mStartYear == 0 && mStartMonth == 0 && mStartDay == 0)) {
            if (mEndYear == 0 && mEndMonth == 0 && mEndDay == 0) {
                dialog.setSelectedDate(mStartYear, mStartMonth, mStartDay);
            } else {
                dialog.setSelectedDate(mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay);
            }
        }
        dialog.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onSelectCompleted(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
                mStartYear = startYear;
                mStartMonth = startMonth;
                mStartDay = startDay;
                mEndYear = endYear;
                mEndMonth = endMonth;
                mEndDay = endDay;

                tv.setText(mStartYear+"/"+mStartMonth+"/"+mStartDay+" - "+mEndYear+"/"+mEndMonth+"/"+mEndDay);
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }
}
