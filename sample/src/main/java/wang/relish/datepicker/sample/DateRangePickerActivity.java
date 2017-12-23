package wang.relish.datepicker.sample;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import wang.relish.colorpicker.ColorPickerDialog;
import wang.relish.datepicker.DatePickerDialog;
import wang.relish.datepicker.MonthStyle;

/**
 * @author Relish Wang
 * @since 2017/3/22
 */
public class DateRangePickerActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSelectedListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    TextView tv;
    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    private boolean mIsWeekShown = false;

    private int mYearTextColor = MonthStyle.YEAR_TEXT_COLOR;
    private int mWeekTextColor = MonthStyle.WEEK_TEXT_COLOR;
    private int mEnabledTextColor = MonthStyle.ENABLED_TEXT_COLOR;
    private int mDisabledTextColor = MonthStyle.DISABLED_TEXT_COLOR;

    private int mTitleBackgroundColor = 0xFAFAFA;
    private int mTitleTextColor = 0x1A1A1A;
    private int mTitleLeftTextColor = 0xFF571A;
    private int mTitleRightTextColor = 0xFF571A;


    private View mYearColorView;
    private View mWeekColorView;
    private View mEnabledColorView;
    private View mDisabledColorView;

    private View mTitleBackgroundColorView;
    private View mTitleTextColorView;
    private View mTitleLeftTextColorView;
    private View mTitleRightTextColorView;


    private SwitchCompat mStWeekShown;

    private RadioGroup mRadioGroup;
    private RadioButton mRb0;
    private RadioButton mRb2;
    private RadioButton mRb3;
    private RadioButton mRb4;
    private RadioButton mRb6;


    private DatePickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mYearTextColor = 0xff571a;
        mWeekTextColor = 0x999999;
        mEnabledTextColor = 0x1a1a1a;
        mDisabledTextColor = 0x1a1a1a;
        dialog = DatePickerDialog.newInstance(this);

        tv = (TextView) findViewById(R.id.tv);
        mStWeekShown = (SwitchCompat) findViewById(R.id.st_week_shown);
        mStWeekShown.setOnCheckedChangeListener(this);

        mRadioGroup = (RadioGroup) findViewById(R.id.rg);
        mRb0 = (RadioButton) findViewById(R.id.rb_0);
        mRb2 = (RadioButton) findViewById(R.id.rb_2);
        mRb3 = (RadioButton) findViewById(R.id.rb_3);
        mRb4 = (RadioButton) findViewById(R.id.rb_4);
        mRb6 = (RadioButton) findViewById(R.id.rb_6);
        mRadioGroup.setOnCheckedChangeListener(this);

        mYearColorView = findViewById(R.id.year_color);
        mWeekColorView = findViewById(R.id.week_color);
        mEnabledColorView = findViewById(R.id.enabled_color);
        mDisabledColorView = findViewById(R.id.disabled_color);
        mTitleBackgroundColorView = findViewById(R.id.title_background_color);
        mTitleTextColorView = findViewById(R.id.title_text_color);
        mTitleLeftTextColorView = findViewById(R.id.title_left_text_color);
        mTitleRightTextColorView = findViewById(R.id.title_right_text_color);

        mYearColorView.setBackgroundColor(mYearTextColor);
        mWeekColorView.setBackgroundColor(mWeekTextColor);
        mEnabledColorView.setBackgroundColor(mEnabledTextColor);
        mDisabledColorView.setBackgroundColor(mDisabledTextColor);

        mTitleBackgroundColorView.setBackgroundColor(mTitleBackgroundColor);
        mTitleTextColorView.setBackgroundColor(mTitleTextColor);
        mTitleLeftTextColorView.setBackgroundColor(mTitleLeftTextColor);
        mTitleRightTextColorView.setBackgroundColor(mTitleRightTextColor);

        mYearColorView.setOnClickListener(this);
        mWeekColorView.setOnClickListener(this);
        mEnabledColorView.setOnClickListener(this);
        mDisabledColorView.setOnClickListener(this);

        mTitleBackgroundColorView.setOnClickListener(this);
        mTitleTextColorView.setOnClickListener(this);
        mTitleLeftTextColorView.setOnClickListener(this);
        mTitleRightTextColorView.setOnClickListener(this);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mStartYear == 0 && mStartMonth == 0 && mStartDay == 0)) {
                    if (mEndYear == 0 && mEndMonth == 0 && mEndDay == 0) {
                        dialog.setSelectedDate(mStartYear, mStartMonth, mStartDay);
                    } else {
                        dialog.setSelectedDate(mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay);
                    }
                }
                dialog.setWeekShownInMonthView(mIsWeekShown);

                dialog.setTitleBarBackgroundColor(mTitleBackgroundColor);//设置标题栏背景颜色
                dialog.setTitleBarTitleTextColor(mTitleTextColor);//设置标题栏标题文字颜色
                dialog.setTitleBarLeftTextColor(mTitleLeftTextColor);//设置标题栏左侧文字颜色
                dialog.setTitleBarRightTextColor(mTitleRightTextColor);//设置标题栏右侧文字颜色
                dialog.setTitleBarBackgroundColor(mTitleBackgroundColor);//设置标题栏背景颜色

                dialog.setYearTextColor(mYearTextColor);
                dialog.setWeekTextColor(mWeekTextColor);
                dialog.setEnabledTextColor(mEnabledTextColor);
                dialog.setDisabledTextColor(mDisabledTextColor);

                dialog.setOnDateSelectedListener(DateRangePickerActivity.this);
                dialog.show();
            }
        });
    }

    @Override
    public void onSelectCompleted(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        mStartYear = startYear;
        mStartMonth = startMonth;
        mStartDay = startDay;
        mEndYear = endYear;
        mEndMonth = endMonth;
        mEndDay = endDay;
        tv.setText(mStartYear + "/" + mStartMonth + "/" + mStartDay + " - " +
                mEndYear + "/" + mEndMonth + "/" + mEndDay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.year_color:
                showColorDialog(mYearTextColor, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mYearTextColor = color;
                        mYearColorView.setBackgroundColor(mYearTextColor);
                    }
                });
                break;
            case R.id.week_color:
                showColorDialog(mWeekTextColor, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mWeekTextColor = color;
                        mWeekColorView.setBackgroundColor(mWeekTextColor);
                    }
                });
                break;
            case R.id.enabled_color:
                showColorDialog(mEnabledTextColor, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mEnabledTextColor = color;
                        mEnabledColorView.setBackgroundColor(mWeekTextColor);
                    }
                });
                break;
            case R.id.disabled_color:
                showColorDialog(mDisabledTextColor, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mDisabledTextColor = color;
                        mDisabledColorView.setBackgroundColor(mTitleBackgroundColor);
                    }
                });
                break;
            case R.id.title_background_color:
                showColorDialog(mYearTextColor, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mTitleBackgroundColor = color;
                        mTitleBackgroundColorView.setBackgroundColor(mTitleBackgroundColor);
                    }
                });
                break;
            case R.id.title_text_color:
                showColorDialog(mWeekTextColor, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mTitleTextColor = color;
                        mTitleTextColorView.setBackgroundColor(mTitleTextColor);
                    }
                });
                break;
            case R.id.title_left_text_color:
                showColorDialog(mEnabledTextColor, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mTitleLeftTextColor = color;
                        mTitleLeftTextColorView.setBackgroundColor(mTitleLeftTextColor);
                    }
                });
                break;
            case R.id.title_right_text_color:
                showColorDialog(mDisabledTextColor, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mTitleRightTextColor = color;
                        mTitleRightTextColorView.setBackgroundColor(mTitleRightTextColor);
                    }
                });
                break;

        }
    }

    private void showColorDialog(int color, ColorPickerDialog.OnColorChangedListener listener) {
        new ColorPickerDialog.Builder(DateRangePickerActivity.this, color)
                .setHexValueEnabled(false)
                .setOnColorChangedListener(listener)
                .build()
                .show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mIsWeekShown = isChecked;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_0:
                dialog = DatePickerDialog.newInstance(this);
                break;
            case R.id.rb_2:
                dialog = DatePickerDialog.newInstance(this, 2016, 2017);
                break;
            case R.id.rb_3:
                dialog = DatePickerDialog.newInstance(this, 2017, 1, 13);
                break;
            case R.id.rb_4:
                dialog = DatePickerDialog.newInstance(this, 2017, 1, 2017, 3);
                break;
            case R.id.rb_6:
                dialog = DatePickerDialog.newInstance(this, 2017, 1, 13, 2017, 3, 2);
                break;
        }
    }
}
