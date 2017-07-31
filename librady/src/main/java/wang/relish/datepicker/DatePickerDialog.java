package wang.relish.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * @author Relish Wang
 * @since 2017/07/31
 */
public class DatePickerDialog extends Dialog implements View.OnClickListener,
        DatePickerAdapter.OnDateSelectedChangedListener {
    /**
     * 被选择的开始日期-结束日期
     */
    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    /**
     * 可选择的最小日期-最大日期
     */
    private int mMinYear, mMinMonth, mMinDay, mMaxYear, mMaxMonth, mMaxDay;

    /**
     * 取消选择日期，完成日期选择，用于显示选择的天数
     */
    private TextView tvCancel, tvComplete, tvDayCount;

    /**
     * 用于显示开始日期/结束日期
     */
    private EditText etStart, etEnd;

    /**
     * 开始时间和结束时间下方的浮标
     */
    private View cursor;

    /**
     * 星期行所在布局
     */
    private LinearLayout mWeekLayout;

    /**
     * 星期行
     */
    private WeekView mWeekView;

    /**
     * 屏幕宽度，游标滑动动画使用
     */
    private int mScreenWidth;

    /**
     * 开始时间的TextInputLayout
     */
    private TextInputLayout inputStart;

    /**
     * 结束时间的TextInputLayout
     */
    private TextInputLayout inputEnd;

    /**
     * 日历
     */
    private RecyclerView mRecyclerView;

    /**
     * 日期选择监听器
     */
    private DatePickerDialog.OnDateSelectedListener mListener;

    /**
     * 日历的适配器
     */
    private DatePickerAdapter mAdapter;

    /**
     * 当前时间减一年 ~ 当前时间<br>
     *
     * @return DatePickerDialog
     */
    public static DatePickerDialog newInstance(Context context) {
        Calendar calendar = Calendar.getInstance();
        int maxYear = calendar.get(Calendar.YEAR);
        int maxMonth = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        // 存在一个很小概率可能触发的bug：
        // 当前时间为闰年2月29日，减一年后变成非闰年，没有29日！
        int monthDays = Utils.getMonthDays(maxYear - 1, maxMonth);
        return newInstance(context, maxYear - 1, maxMonth, nowDay > monthDays ? monthDays : nowDay, maxYear, maxMonth, nowDay);
    }

    /**
     * 1970年1月1日 ~ 输入年月
     *
     * @param year  结束年
     * @param month 结束月
     * @return DatePickerDialog
     */
    public static DatePickerDialog newInstance(Context context, int year, int month) {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("There is no month called " + month + " on the earth!");
        }
        int monthDays = Utils.getMonthDays(year, month);
        return newInstance(context, 1970, 1, 1, year, month, monthDays);
    }

    /**
     * 1970年1月1日 ~ 输入年月日
     *
     * @param maxYear  最大年
     * @param maxMonth 最大月
     * @param maxDay   最大日
     * @return DatePickerDialog
     */
    public static DatePickerDialog newInstance(Context context, int maxYear, int maxMonth, int maxDay) {
        if (Utils.isDayCorrect(maxYear, maxMonth, maxDay)) {
            return newInstance(context, 1970, 1, 1, maxYear, maxMonth, maxDay);
        } else {
            throw new IndexOutOfBoundsException(
                    maxYear + "-" + maxMonth + "-" + maxDay + " is not a correct date.");
        }
    }

    /**
     * 开始年开始月 ~ 结束年结束月
     *
     * @param minYear  最小年
     * @param minMonth 最小年对应的最小月
     * @param maxYear  最大年
     * @param maxMonth 最大年对应的最大月
     * @return DatePickerDialog
     */
    public static DatePickerDialog newInstance(Context context, int minYear, int minMonth, int maxYear, int maxMonth) {
        if (minMonth < 1 || minMonth > 12) {
            throw new IndexOutOfBoundsException("There is no month called " + minMonth + " on the earth!");
        }
        if (maxMonth < 1 || maxMonth > 12) {
            throw new IndexOutOfBoundsException("There is no month called " + maxMonth + " on the earth!");
        }
        int monthDays = Utils.getMonthDays(maxYear, maxMonth);
        return newInstance(context, minYear, minMonth, 1, maxYear, maxMonth, monthDays);
    }


    /**
     * 开始年开始月开始日 ~ 结束年结束月结束日
     *
     * @param minYear  最小年
     * @param minMonth 最小年对应的最小月
     * @param minDay   最小年月对应的最小日
     * @param maxYear  最大年
     * @param maxMonth 最大年对应的最大月
     * @param maxDay   最大年月对应的最大日
     * @return DatePickerDialog
     */
    public static DatePickerDialog newInstance(Context context, int minYear, int minMonth, int minDay, int maxYear, int maxMonth, int maxDay) {
        if (!Utils.isDayCorrect(minYear, minMonth, minDay)) {
            throw new IndexOutOfBoundsException(
                    minYear + "-" + minMonth + "-" + minDay + " is not a correct date.");
        }
        if (!Utils.isDayCorrect(maxYear, maxMonth, maxDay)) {
            throw new IndexOutOfBoundsException(
                    maxYear + "-" + maxMonth + "-" + maxDay + " is not a correct date.");
        }
        if ((maxYear > minYear) || (maxYear == minYear && maxMonth > minMonth) || (maxYear == minYear && maxMonth == minMonth && maxDay >= minDay)) {
            return new DatePickerDialog(context, minYear, minMonth, minDay, maxYear, maxMonth, maxDay);
        } else {
            throw new InvalidParameterException("Minimum date should not later than maximal date：" +
                    minYear + "年" + minMonth + "月" + minDay + "日" +
                    " > " +
                    maxYear + "年" + maxMonth + "月" + maxDay + "日");
        }
    }


    public DatePickerDialog(Context context, int minYear, int minMonth, int minDay, int maxYear, int maxMonth, int maxDay) {
        super(context, R.style.BottomDialog);
        mMinYear = minYear;
        mMinMonth = minMonth;
        mMinDay = minDay;
        mMaxYear = maxYear;
        mMaxMonth = maxMonth;
        mMaxDay = maxDay;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        setContentView(R.layout.dialog_date_picker);
        initViews();
        setCanceledOnTouchOutside(false);// 点击Dialog外部消失
    }

    @SuppressWarnings("deprecation")
    private void initViews() {

        RelativeLayout titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvComplete = (TextView) findViewById(R.id.tv_complete);
        tvDayCount = (TextView) findViewById(R.id.tv_day_count);

        mYearTextSize = mYearTextSize == 0 ? Utils.dp2px(getContext(), MonthStyle.YEAR_TEXT_SIZE) : mYearTextSize;
        mWeekTextSize = mWeekTextSize == 0 ? Utils.dp2px(getContext(), MonthStyle.WEEK_TEXT_SIZE) : mWeekTextSize;
        mEnabledTextSize = mEnabledTextSize == 0 ? Utils.dp2px(getContext(), MonthStyle.ENABLE_TEXT_SIZE) : mEnabledTextSize;
        mDisabledTextSize = mDisabledTextSize == 0 ? Utils.dp2px(getContext(), MonthStyle.DISABLE_TEXT_SIZE) : mDisabledTextSize;


        mScreenWidth = Utils.getScreenWidth(getContext());

        if (mTitleBarBackgroundColor != 0) titleBar.setBackgroundColor(mTitleBarBackgroundColor);
        if (mTitleBarLeftTextColor != 0) tvCancel.setTextColor(mTitleBarLeftTextColor);
        if (mTitleBarRightTextColor != 0) tvComplete.setTextColor(mTitleBarRightTextColor);
        if (mTitleBarTitleTextColor != 0) tvDayCount.setTextColor(mTitleBarTitleTextColor);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvComplete.setOnClickListener(this);

        inputStart = (TextInputLayout) findViewById(R.id.input_start);
        inputEnd = (TextInputLayout) findViewById(R.id.input_end);

        inputStart.setHintTextAppearance(R.style.TextAppearance_Hint);
        inputEnd.setHintTextAppearance(R.style.TextAppearance_Hint);

        EditText etFocus = (EditText) findViewById(R.id.et_focus);
        etFocus.setEnabled(false);
        etFocus.setKeyListener(null);

        etStart = (EditText) findViewById(R.id.tv_start);
        etEnd = (EditText) findViewById(R.id.tv_end);
        etStart.setEnabled(false);
        etEnd.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            etStart.setBackground(null);
            etEnd.setBackground(null);
        } else {
            etStart.setBackgroundDrawable(null);
            etEnd.setBackgroundDrawable(null);
        }
        etStart.setKeyListener(null);//禁用输入框
        etEnd.setKeyListener(null);//禁用输入框
        etStart.addTextChangedListener(new DatePickerDialog.ChangeTextSizeWatcher(etStart));
        etEnd.addTextChangedListener(new DatePickerDialog.ChangeTextSizeWatcher(etEnd));

        cursor = findViewById(R.id.cursor);

        mWeekLayout = (LinearLayout) findViewById(R.id.week);
        mWeekView = (WeekView) findViewById(R.id.weekView);

        mWeekView.setWeekTextColor(mWeekTextColor);
        mWeekView.setWeekTextSize(mWeekTextSize);
        mWeekLayout.setVisibility(mIsWeekShown ? View.GONE : View.VISIBLE);

        //日期选择适配器
        mAdapter = new DatePickerAdapter();
        mAdapter.setWeekShownInMonthView(mIsWeekShown);//是否不使用星期行
        mAdapter.setYearTextSize(mYearTextSize);
        mAdapter.setYearTextColor(mYearTextColor);
        mAdapter.setWeekTextSize(mWeekTextSize);
        mAdapter.setWeekTextColor(mWeekTextColor);
        mAdapter.setEnabledTextSize(mEnabledTextSize);
        mAdapter.setEnabledTextColor(mEnabledTextColor);
        mAdapter.setDisabledTextSize(mDisabledTextSize);
        mAdapter.setDisabledTextColor(mDisabledTextColor);
        mAdapter.setDateRange(mMinYear, mMinMonth, mMinDay, mMaxYear, mMaxMonth, mMaxDay);//设置日期范围
        mAdapter.setOnSelectedChangedListener(this);//设置日期选择回调，用于显示当前选择的日期范围
        if (mStartYear != 0 && mStartMonth != 0 && mStartDay != 0) {
            mAdapter.setSelectedDate(mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.datePicker);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        //如果未选择时间，则默认显示到当前时间
        if (mMaxYear > nowYear || (mMaxYear == nowYear && mMaxMonth >= nowMonth)) {
            int position = DatePickerAdapter.getPosition(mMinYear, mMinMonth, mMaxYear, mMaxMonth, nowYear, nowMonth);
            mRecyclerView.scrollToPosition(position);
        }

        if (mStartYear != 0 && mStartMonth != 0 && mStartDay != 0) {
            Animation animation = new TranslateAnimation(0, mScreenWidth / 2, 0, 0);
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);

            int position = DatePickerAdapter.getPosition(mMinYear, mMinMonth, mMaxYear, mMaxMonth, mStartYear, mStartMonth);
            mRecyclerView.scrollToPosition(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvComplete) {
            if (mListener != null) {
                if (mStartYear == 0 && mStartMonth == 0 && mStartDay == 0) {
                    Toast.makeText(getContext(), "您未选择正确的时间", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mEndYear == 0 && mEndMonth == 0 && mEndDay == 0) {
                        mListener.onSelectCompleted(mStartYear, mStartMonth, mStartDay, mStartYear, mStartMonth, mStartDay);
                    } else {
                        mListener.onSelectCompleted(mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay);
                    }
                }
            }
            dismiss();
        }
    }

    /**
     * 重置选择的时间
     */
    public void resetSelectedDate() {
        mStartYear = 0;
        mStartMonth = 0;
        mStartDay = 0;
        mEndYear = 0;
        mEndMonth = 0;
        mEndDay = 0;
    }

    /**
     * 时间选择改变回调
     *
     * @param daySpan 时间区间
     */
    @Override
    public void onSelectedChanged(int... daySpan) {
        int len = daySpan.length;
        if (len == 3) {
            if (mStartYear == 0 && mStartMonth == 0 && mStartDay == 0) {
                Animation animation = new TranslateAnimation(0, mScreenWidth / 2, 0, 0);
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
            }

            mStartYear = daySpan[0];
            mStartMonth = daySpan[1];
            mStartDay = daySpan[2];
            tvDayCount.setText("已选择1天");
            etStart.setText(mStartYear + "年" + mStartMonth + "月" + mStartDay + "日");
            etEnd.setText("");

            LinearLayout.LayoutParams layoutParamsStart = (LinearLayout.LayoutParams) inputStart.getLayoutParams();
            layoutParamsStart.setMargins(0, Utils.dp2px(getContext(), 16f), 0, 0);//4个参数按顺序分别是左上右下
            inputStart.setLayoutParams(layoutParamsStart);

            LinearLayout.LayoutParams layoutParamsEnd = (LinearLayout.LayoutParams) inputEnd.getLayoutParams();
            layoutParamsEnd.setMargins(0, 0, 0, Utils.dp2px(getContext(), 3f));//4个参数按顺序分别是左上右下
            inputEnd.setLayoutParams(layoutParamsEnd);

            mEndYear = 0;
            mEndMonth = 0;
            mEndDay = 0;


        } else if (len == 6) {
            mStartYear = daySpan[0];
            mStartMonth = daySpan[1];
            mStartDay = daySpan[2];
            mEndYear = daySpan[3];
            mEndMonth = daySpan[4];
            mEndDay = daySpan[5];
            int dayCount = Utils.getDayNum(
                    mStartYear, mStartMonth, mStartDay,//开始日期
                    mEndYear, mEndMonth, mEndDay);//终止日期
            tvDayCount.setText("已选择" + dayCount + "天");
            etStart.setText(mStartYear + "年" + mStartMonth + "月" + mStartDay + "日");
            etEnd.setText(mEndYear + "年" + mEndMonth + "月" + mEndDay + "日");

            LinearLayout.LayoutParams layoutParamsStart = (LinearLayout.LayoutParams) inputStart.getLayoutParams();
            layoutParamsStart.setMargins(0, Utils.dp2px(getContext(), 16f), 0, 0);//4个参数按顺序分别是左上右下
            inputStart.setLayoutParams(layoutParamsStart);

            LinearLayout.LayoutParams layoutParamsEnd = (LinearLayout.LayoutParams) inputEnd.getLayoutParams();
            layoutParamsEnd.setMargins(0, Utils.dp2px(getContext(), 16f), 0, 0);//4个参数按顺序分别是左上右下
            inputEnd.setLayoutParams(layoutParamsEnd);
        } else {
            resetSelectedDate();
            throw new UnsupportedOperationException("暂不支持处理" + len + "个参数的输入");
        }
    }

    /**
     * 设置选定的时间区间
     *
     * @param startYear  开始年
     * @param startMonth 开始月
     * @param startDay   开始日
     */
    public void setSelectedDate(int startYear, int startMonth, int startDay) {
        if (!Utils.isDayCorrect(startYear, startMonth, startDay)) {
            resetSelectedDate();
            return;
        }
        mStartYear = startYear;
        mStartMonth = startMonth;
        mStartDay = startDay;
        mEndYear = startYear;
        mEndMonth = startMonth;
        mEndDay = startDay;
    }

    /**
     * 设置选定的时间区间
     *
     * @param startYear  开始年
     * @param startMonth 开始月
     * @param startDay   开始日
     * @param endYear    结束年
     * @param endMonth   结束月
     * @param endDay     结束日
     */
    public void setSelectedDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        if (!Utils.isDayCorrect(startYear, startMonth, startDay)) {
            resetSelectedDate();
            return;
        }
        mStartYear = startYear;
        mStartMonth = startMonth;
        mStartDay = startDay;
        mEndYear = endYear;
        mEndMonth = endMonth;
        mEndDay = endDay;
    }

    private int mYearTextColor;
    private int mYearTextSize;
    private int mWeekTextColor;
    private int mWeekTextSize;
    private int mEnabledTextColor;
    private int mEnabledTextSize;
    private int mDisabledTextColor;
    private int mDisabledTextSize;
    private boolean mIsWeekShown = true;

    /**
     * 设置MonthView中是否显示星期行
     *
     * @param isWeekShown 是否显示星期行
     */
    public void setWeekShownInMonthView(boolean isWeekShown) {
        this.mIsWeekShown = isWeekShown;
    }

    /**
     * 设置MonthView中显示年月的文字的颜色
     *
     * @param yearTextColor 显示XXXX年XX月的文字的颜色
     */
    public void setYearTextColor(int yearTextColor) {
        this.mYearTextColor = yearTextColor;
    }

    /**
     * 设置MonthView中显示年月的文字的大小
     *
     * @param yearTextSize 显示XXXX年XX月的文字的大小
     * @deprecated 有错误，暂不提供支持
     */
    private void setYearTextSize(int yearTextSize) {
        this.mYearTextSize = yearTextSize;
    }

    /**
     * 设置MonthView中显示周日~周六的文字的颜色
     *
     * @param weekTextColor 显示周日~周六的文字的颜色
     */
    public void setWeekTextColor(int weekTextColor) {
        this.mWeekTextColor = weekTextColor;
    }

    /**
     * 设置MonthView中显示周日~周六的文字的大小
     *
     * @param weekTextSize 显示周日~周六的文字的大小
     * @deprecated 有错误，暂不提供支持
     */
    private void setWeekTextSize(int weekTextSize) {
        this.mWeekTextSize = weekTextSize;
    }


    private int mTitleBarBackgroundColor;
    private int mTitleBarTitleTextColor;
    private int mTitleBarLeftTextColor;
    private int mTitleBarRightTextColor;

    public void setTitleBarBackgroundColor(int color) {
        mTitleBarBackgroundColor = color;
    }

    public void setTitleBarTitleTextColor(int color) {
        mTitleBarTitleTextColor = color;
    }

    public void setTitleBarLeftTextColor(int color) {
        mTitleBarLeftTextColor = color;
    }

    public void setTitleBarRightTextColor(int color) {
        mTitleBarRightTextColor = color;
    }

    /**
     * 设置MonthView中可以被选择的日期的文字的颜色
     *
     * @param enabledTextColor 可以被选择的日期的文字的颜色
     */
    public void setEnabledTextColor(int enabledTextColor) {
        this.mEnabledTextColor = enabledTextColor;
    }

    /**
     * 设置MonthView中可以被选择的日期的文字的大小
     *
     * @param enableTextSize 可以被选择的日期的文字的大小
     * @deprecated 有错误，暂不提供支持
     */
    private void setEnabledTextSize(int enableTextSize) {
        this.mEnabledTextSize = enableTextSize;
    }

    /**
     * 设置MonthView中不可被选择的日期的文字的颜色
     *
     * @param disabledTextColor 不可被选择的日期的文字的颜色
     */
    public void setDisabledTextColor(int disabledTextColor) {
        this.mDisabledTextColor = disabledTextColor;
    }

    /**
     * 设置MonthView中不可被选择的日期的文字的大小
     *
     * @param disabledTextSize 不可被选择的日期的文字的大小
     * @deprecated 有错误，暂不提供支持
     */
    private void setDisabledTextSize(int disabledTextSize) {
        this.mDisabledTextSize = disabledTextSize;
    }

    /**
     * 选择可选的时间范围
     *
     * @param minYear  最小年
     * @param minMonth 最小年对应的最小月
     * @param minDay   最小年月对应的最小日
     * @param maxYear  最大年
     * @param maxMonth 最大年对应的最大月
     * @param maxDay   最大年月对应的最大日
     */
    public void setDateRange(int minYear, int minMonth, int minDay, int maxYear, int maxMonth, int maxDay) {
        mMinYear = minYear;
        mMinMonth = minMonth;
        mMinDay = minDay;
        mMaxYear = maxYear;
        mMaxMonth = maxMonth;
        mMaxDay = maxDay;
    }

    /**
     * 设置日期选择监听器
     *
     * @param listener 监听器
     */
    public void setOnDateSelectedListener(DatePickerDialog.OnDateSelectedListener listener) {
        mListener = listener;
    }

    /**
     * Dialog设置时间设置回调监听器
     */
    public interface OnDateSelectedListener {
        /**
         * Dialog完成日期范围选择回调
         *
         * @param startYear  开始年
         * @param startMonth 开始月
         * @param startDay   开始日
         * @param endYear    结束年
         * @param endMonth   结束月
         * @param endDay     结束日
         */
        void onSelectCompleted(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay);
    }

    /**
     * 应设计师要求，有日期被选择时，EditText#textSize变小
     */
    private class ChangeTextSizeWatcher implements TextWatcher {

        private EditText et;

        public ChangeTextSizeWatcher(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = et.getText().toString();
            if (TextUtils.isEmpty(text)) {
                et.setTextSize(Utils.dp2px(getContext(), 10));
            } else {
                et.setTextSize(Utils.dp2px(getContext(), 6));
            }
        }
    }

    @Override
    public void show() {
        super.show();
        initViews();
    }
}
