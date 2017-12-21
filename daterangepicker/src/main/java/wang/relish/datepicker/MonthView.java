package wang.relish.datepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

import wang.relish.datepicker.drawable.CustomDrawable;
import wang.relish.datepicker.drawable.TodayDrawable;

/**
 * 显示月份
 *
 * @author Relish Wang
 * @since 2017/3/15
 */

public final class MonthView extends View {

    /**
     * 默认背景颜色
     */
    private int WHITE_COLOR = Color.parseColor("#FFFFFF");


    private MonthStyle mMonthStyle;


    private OnSelectListener mListener;

    private final int NUM_COLUMNS = 7;
    private int NUM_ROWS = 6;
    private static int BOTTOM_PADDING = 48;
    private Paint mPaint;
    private int mDateHeight;
    /**
     * 现在的现实时间
     */
    private int mNowYear, mNowMonth, mNowDay;
    private int mCurrYear, mCurrMonth;
    /**
     * 格子变成
     */
    private float mCellSize;

    private float mYearCellHeight;

    private float mWeekCellHeight;

    private boolean isWeekShown = true;

    private int mDaySize;

    /**
     * 可选择时间区域
     */
    private int mMinYear, mMinMonth, mMinDay, mMaxYear, mMaxMonth, mMaxDay;

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);//写成super(context);的话，在findViewById会得到null
        setBackgroundColor(Color.parseColor("#ffffff"));
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mDaySize = (int) typedArray.getDimension(R.styleable.CalendarView_DateSize, 35);
        mDateHeight = (int) typedArray.getDimension(R.styleable.CalendarView_DateHeight, 66);
        typedArray.recycle();


        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNowYear = calendar.get(Calendar.YEAR);
        mNowMonth = calendar.get(Calendar.MONTH) + 1;
        mNowDay = calendar.get(Calendar.DATE);
        if (mCurrYear == 0 && mCurrMonth == 0) {
            setYearMonth(mNowYear, mNowMonth);
        }

        if (mMonthStyle == null) {
            mMonthStyle = new MonthStyle();

            mMonthStyle.setWeekTextColor(MonthStyle.WEEK_TEXT_COLOR);
            mMonthStyle.setWeekTextSize(MonthStyle.WEEK_TEXT_SIZE);
            mMonthStyle.setYearTextColor(MonthStyle.YEAR_TEXT_COLOR);
            mMonthStyle.setYearTextSize(MonthStyle.YEAR_TEXT_SIZE);
            boolean isSameYearMonth = mCurrYear == mNowYear && mCurrMonth == mNowMonth;
            //这个月有多少天
            int mMonthDays = Utils.getMonthDays(mCurrYear, mCurrMonth + 1);
            DateStyle[] dateStyles = new DateStyle[mMonthDays];
            for (int i = 0; i < mMonthDays; i++) {
                int day = i + 1;
                DateStyle item = new DateStyle(day + "");
                item.setTextSize(item.getTextSize() == 0 ? mDaySize : item.getTextSize());
                item.setTextColor(item.getTextColor());
                if (isSameYearMonth && mNowDay == day) {
                    //第一版设计被毙了
//                    item.setTextColor(MonthStyle.TODAY_TEXT_COLOR);
//                    item.setText("今天");
                    TodayDrawable drawable = new TodayDrawable();
                    item.setDrawable(drawable);
                } else {
                    item.setDrawable(null);
                }
                dateStyles[i] = item;
            }
            mMonthStyle.setDateStyles(dateStyles);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        NUM_ROWS = getMonthRowNumber();
        BOTTOM_PADDING = widthSize * 48 / 750;
        float cellSize = widthSize / NUM_COLUMNS;
        float yHeight = mYearCellHeight == 0 ? cellSize : mYearCellHeight;
        float wHeight = mWeekCellHeight == 0 ? cellSize : mWeekCellHeight;
        int heightSize = (int) (NUM_ROWS * cellSize + yHeight + (isWeekShown ? wHeight : 0) + BOTTOM_PADDING);
        int measureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        setMeasuredDimension(measureSpec, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCellSize = getWidth() * 1.0F / NUM_COLUMNS;
        mYearCellHeight = mYearCellHeight == 0 ? mCellSize : mYearCellHeight;//TODO 临时 以后开发接口，可以给使用者设置
        mWeekCellHeight = mWeekCellHeight == 0 ? mCellSize : mWeekCellHeight;//TODO 临时 以后开发接口，可以给使用者设置

        //--------------------------------绘制标题---------------------------------------------------
        String title = mCurrYear + "年" + (mCurrMonth + 1) + "月";
        mPaint.setTextSize(mMonthStyle.getYearTextSize() == 0 ? mDaySize : mMonthStyle.getYearTextSize());
        double titleLength = mPaint.measureText(title);//用于居中显示时使用
        int startTitleX = Utils.dp2px(getContext(), 16);
        int startTitleY = (int) (mCellSize * 84 / 107);//mDateHeight;
        mPaint.setColor(mMonthStyle.getYearTextColor() == 0 ? MonthStyle.YEAR_TEXT_COLOR : mMonthStyle.getYearTextColor());
        canvas.drawText(title, startTitleX, startTitleY, mPaint);

        //--------------------------------绘制星期---------------------------------------------------
        //这个月有多少天
        int mMonthDays = Utils.getMonthDays(mCurrYear, mCurrMonth + 1);
        //当月1日是周几
        int weekNumber = Utils.getFirstDayWeek(mCurrYear, mCurrMonth);
        int column, row;
        if (isWeekShown) {
            //周日~周六的字符串数组
            String[] weekString = getContext().getResources().getStringArray(R.array.weekday);
            for (int i = 0; i < weekString.length; i++) {
                //绘制白色背景矩形
                float startRecX = mCellSize * i;
                float startRecY = mYearCellHeight;
                float endRecX = startRecX + mWeekCellHeight;
                float endRecY = startRecY + mWeekCellHeight;
                mPaint.setColor(WHITE_COLOR);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);

                mPaint.setTextSize(mMonthStyle.getWeekTextSize() == 0 ? mDaySize : mMonthStyle.getWeekTextSize());
                float startX = startRecX + (mCellSize - mPaint.measureText(weekString[i])) / 2;
                float startY = startRecY + mCellSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2;
                mPaint.setColor(mMonthStyle.getWeekTextColor() == 0 ? MonthStyle.WEEK_TEXT_COLOR : mMonthStyle.getWeekTextColor());
                canvas.drawText(weekString[i], startX, startY, mPaint);
            }
        }

        //----------------------------------绘制具体日期----------------------------------------------
        for (int day = 0; day < mMonthDays; day++) {
            column = (day + weekNumber - 1) % 7;
            row = (day + weekNumber - 1) / 7;

            //绘制白色背景矩形背景
            float startRecX = mCellSize * column;
            float startRecY = mCellSize * row + mYearCellHeight + (isWeekShown ? mWeekCellHeight : 0);
            float endRecX = startRecX + mCellSize;
            float endRecY = startRecY + mCellSize;
            mPaint.setColor(WHITE_COLOR);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);

            //绘制日期样式及其文字
            DateStyle item = mMonthStyle.getDateStyles()[day];
            CustomDrawable drawable = item.getDrawable();
            if (drawable != null) {
                drawable.setCellSize((int) mCellSize + 1);//+1为了弥补取整时缺失的像素，视觉难以察觉
                drawable.setPosition((int) (startRecX), (int) (startRecY));
                drawable.setBounds(0, 0, (int) mCellSize, (int) mCellSize);
                drawable.draw(canvas);
            }
            mPaint.setTextSize(item.getTextSize());
            mPaint.setColor(item.getTextColor());
            float startX = startRecX + (mCellSize - mPaint.measureText(item.getText())) / 2;
            float startY = startRecY + mCellSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2;
            canvas.drawText(item.getText(), startX, startY, mPaint);
        }
    }

    /**
     * 获取总共行数
     *
     * @return
     */
    private int getMonthRowNumber() {
        int mMonthDays = Utils.getMonthDays(mCurrYear, mCurrMonth + 1);
        int weekNumber = Utils.getFirstDayWeek(mCurrYear, mCurrMonth);
        return (mMonthDays + weekNumber - 1) % 7 == 0 ? (mMonthDays + weekNumber - 1) / 7 : (mMonthDays + weekNumber - 1) / 7 + 1;
    }

    /**
     * 设置选中的月份
     *
     * @param year  年份
     * @param month 实际月份（接收的月份为1-12）
     */
    public void setYearMonth(int year, int month) {
        mCurrYear = year;
        mCurrMonth = month - 1;
    }

    /**
     * 设置日期选择回调的监听器
     *
     * @param listener 日期选择回调监听器
     */
    public void setSelectListener(OnSelectListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            float wHeight = (isWeekShown ? mWeekCellHeight : 0);
            if (y < wHeight + mYearCellHeight) {
                return false;
            } else {
                int cellX = (int) (x / mCellSize) + 1;
                int cellY = (int) ((y - mYearCellHeight - wHeight) / mCellSize);
                int weekDay = Utils.getFirstDayWeek(mCurrYear, mCurrMonth);//-1：为了这个对应关系【1~7:周日~周六】
                int day = cellY * 7 + cellX - weekDay + 1;
                boolean isDayCorrect = Utils.isDayCorrect(mCurrYear, mCurrMonth + 1, day);
                boolean enable = Utils.isWithinDateSpan(mMinYear, mMinMonth, mMinDay, mMaxYear, mMaxMonth, mMaxDay, mCurrYear, mCurrMonth + 1, day);
                if (mListener != null && isDayCorrect && enable) {
                    mListener.onSelect(this, mCurrYear, mCurrMonth + 1, day);
                    invalidate();
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    /**
     * 设置可选择的日期范围
     * <p>
     * 可接受的参数格式：<br>
     * <ul>
     * <li>2: 下限年1月1日 ~ 上限年12月31日</li>
     * <li>4: 下限年月1日 ~ 上限年月当月最大日</li>
     * <li>6: 下限年月日 ~ 上限年月日</li>
     * </ul>
     *
     * @param dateSpan 日期范围
     */
    public void setDateRange(int... dateSpan) {
        int len = dateSpan.length;
        if (len == 2) {
            mMinYear = dateSpan[0];
            mMinMonth = 1;
            mMinDay = 1;
            mMaxYear = dateSpan[1];
            mMaxMonth = 12;
            mMaxDay = 31;
        } else if (len == 4) {
            mMinYear = dateSpan[0];
            mMinMonth = dateSpan[1];
            mMinDay = 1;
            mMaxYear = dateSpan[2];
            mMaxMonth = dateSpan[3];
            mMaxDay = Utils.getMonthDays(mMaxYear, mMaxMonth);
        } else if (len == 6) {
            mMinYear = dateSpan[0];
            mMinMonth = dateSpan[1];
            mMinDay = dateSpan[2];
            mMaxYear = dateSpan[3];
            mMaxMonth = dateSpan[4];
            mMaxDay = dateSpan[5];
        } else {
            throw new UnsupportedOperationException("暂不支持处理" + len + "个参数的入参");
        }
    }

    /**
     * MonthView设置触摸回调
     */
    interface OnSelectListener {
        /**
         * 选择了某个日期
         *
         * @param monthView 被点击的MonthView
         * @param year      年份
         * @param month     月份
         * @param day       日期
         */
        void onSelect(MonthView monthView, int year, int month, int day);
    }

    public int getTextSize() {
        return mDaySize;
    }

    /**
     * 设置所有的Item属性
     *
     * @param monthStyle Item们的属性
     */
    public void setMonthStyle(MonthStyle monthStyle) {
        mMonthStyle = monthStyle;
    }


    /**
     * 设置星期是否可见
     *
     * @param isShown 是否可见
     */
    public void setIsWeekShown(boolean isShown) {
        isWeekShown = isShown;
        invalidate();
    }
}
